package com.invoiceconverter.invoiceconverter.configuration;

import com.invoiceconverter.invoiceconverter.model.Branch;
import com.invoiceconverter.invoiceconverter.repository.BranchRepository;
import org.aopalliance.aop.Advice;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.SourcePollingChannelAdapterSpec;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.filters.ChainFileListFilter;
import org.springframework.integration.file.filters.FileSystemPersistentAcceptOnceFileListFilter;
import org.springframework.integration.file.filters.RegexPatternFileListFilter;
import org.springframework.integration.file.remote.session.DefaultSessionFactoryLocator;
import org.springframework.integration.file.remote.session.DelegatingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.remote.session.SessionFactoryLocator;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.ftp.dsl.Ftp;
import org.springframework.integration.ftp.dsl.FtpInboundChannelAdapterSpec;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.handler.advice.ExpressionEvaluatingRequestHandlerAdvice;

import org.springframework.integration.jdbc.metadata.JdbcMetadataStore;
import org.springframework.integration.metadata.ConcurrentMetadataStore;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;


import static org.apache.logging.log4j.LogManager.getLogger;

@Configuration
@EnableIntegration
@ComponentScan
public class FTPIntegration {

    public static final String TEMPORARY_FILE_SUFFIX = ".part";
    public static final int POLLER_FIXED_PERIOD_DELAY = 5000;
    public static final int MAX_MESSAGES_PER_POLL = 100;

    private static final Logger LOG = getLogger(FTPIntegration.class);

    private DataSource dataSource;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    private Job ipJob;

    Map<Object, SessionFactory<FTPFile>> factories = new HashMap<Object, SessionFactory<FTPFile>>();
    DefaultSessionFactoryLocator<FTPFile> defaultSessionFactoryLocator = new DefaultSessionFactoryLocator<FTPFile>(factories);

    @Bean
    public Branch myBranch() {
        return new Branch();
    }


    public FTPIntegration() {
    }

    /**
     * The default poller with 5s, 100 messages , will poll the FTP folder location
     *
     * @return default poller.
     */
    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller(){
        return Pollers
                .fixedDelay(POLLER_FIXED_PERIOD_DELAY)
                .maxMessagesPerPoll(MAX_MESSAGES_PER_POLL)
                .transactional()
                .get();
    }

    /**
     * The direct channel for the flow.
     *
     * @return MessageChannel
     */
    @Bean
    public MessageChannel ipIntermediateChannel() {
        return new DirectChannel();
    }

/**
 * Method that creates a flow to read from FTP server the .ip files
 * and transform it to a local folder with the name of the branch.
 *
 * @return IntegrationFlow
 */

    public IntegrationFlow fileInboundFlowFromFTPServer(Branch branch){

        final FtpInboundChannelAdapterSpec sourceSpecFtp = Ftp.inboundAdapter(createNewFtpSessionFactory(branch))
                .preserveTimestamp(true)
                .patternFilter("*.ip")
                .maxFetchSize(MAX_MESSAGES_PER_POLL)
                .remoteDirectory(branch.getFolderPathIn())
                .deleteRemoteFiles(true)
                .localDirectory(new File(branch.getBranchCode()))
                .temporaryFileSuffix(TEMPORARY_FILE_SUFFIX);

        final Consumer<SourcePollingChannelAdapterSpec> ipInboundPoller = endpointConfigurer -> endpointConfigurer
                .id("ipInboundPoller")
                .autoStartup(true)
                .poller(poller());

        IntegrationFlow flow = IntegrationFlows
                .from(sourceSpecFtp, ipInboundPoller)
                .transform(File.class, p -> {
                    // log step
                    LOG.info("flow=ipInboundFlowFromFTP, message=incoming file: " + p);
                    return p;})
                .handle( message ->{LOG.info("Handling message : " + message);} )
                .get();

        return flow;
    }

    /**
     * Creating the outbound adaptor to send files from local to FTP server
     *@return integration flow from local to FTP server
     */

    public IntegrationFlow localToFtpFlow(Branch myBranch) {

        return IntegrationFlows.from(Files.inboundAdapter(new File(myBranch.getBranchCode()))
                        .filter(new ChainFileListFilter<File>()
                                //.addFilter(new RegexPatternFileListFilter("final" + myBranch.getBranchCode() + ".csv"))
                                .addFilter(new RegexPatternFileListFilter(".*\\.ip"))
                                .addFilter(new FileSystemPersistentAcceptOnceFileListFilter(metadataStore(dataSource), "foo"))),//FileSystemPersistentAcceptOnceFileListFilter
                e -> e.poller(Pollers.fixedDelay(10_000)))
                //.enrichHeaders(h ->h.headerExpression("file_originalFile", "new java.io.File('"+ myBranch.getBranchCode() +"/FEFOexport" + myBranch.getBranchCode() + ".csv')",true))
                //.enrichHeaders(h ->h.headerExpression("file_originalFile", "new java.io.File('"+ myBranch.getBranchCode() + "/BOM9007134_1568194686033.ip')",true))
                .transform(p -> {
                    LOG.info("Sending file " + p + " to FTP branch " + myBranch.getBranchCode());
                    return p;
                })

                //.log()
                .transform(m -> {
                    this.defaultSessionFactoryLocator.addSessionFactory(myBranch.getBranchCode(),createNewFtpSessionFactory(myBranch));
                    LOG.info("Adding factory to delegation");
                    return m;
                })
                .publishSubscribeChannel(s ->
                        s.subscribe(h -> h.handle(Ftp.outboundAdapter(createNewFtpSessionFactory(myBranch), FileExistsMode.REPLACE)
                                .useTemporaryFileName(true)
                                .autoCreateDirectory(false)
                                .remoteDirectory(myBranch.getFolderPathOut()), e -> e.advice(expressionAdvice())))
                                .subscribe(f ->f.transform(fileMessageToJobRequest())
                                        .handle(jobLaunchingGateway()).channel("nullChannel")))
                .handle(jobExecution -> {
                    System.out.println("Payload is: " + jobExecution.getPayload());
                })
                /*.handle(Ftp.outboundAdapter(createNewFtpSessionFactory(myBranch), FileExistsMode.REPLACE)
                        .useTemporaryFileName(true)
                        .autoCreateDirectory(false)
                        .remoteDirectory(myBranch.getFolderPathOut()), e -> e.advice(expressionAdvice()))*/
                .get();
    }


    @Bean
    public FileMessageToJobRequest fileMessageToJobRequest(){
        FileMessageToJobRequest fileMessageToJobRequest = new FileMessageToJobRequest();
        fileMessageToJobRequest.setFileParameterName("file_path");
        fileMessageToJobRequest.setJob(ipJob);
        return fileMessageToJobRequest;
    }

    //@Bean
    public JobLaunchingGateway jobLaunchingGateway() {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        simpleJobLauncher.setTaskExecutor(new SyncTaskExecutor());
        JobLaunchingGateway jobLaunchingGateway = new JobLaunchingGateway(simpleJobLauncher);

        return jobLaunchingGateway;
    }

    /**
     * Creating FTP connection based on the branch ftp data entered.
     * @return ftpSessionFactory
     */

    private DefaultFtpSessionFactory createNewFtpSessionFactory(Branch branch) {
        final DefaultFtpSessionFactory factoryLocator = new DefaultFtpSessionFactory();
        factoryLocator.setHost(branch.getHost());
        factoryLocator.setUsername(branch.getUsern());
        factoryLocator.setPort(branch.getFtpPort());
        factoryLocator.setPassword(branch.getPassword());
        return factoryLocator;
    }

    /**
     * Creating the advice for routing the payload of the outbound message on different expressions (success, failure)
     * @return Advice
     */

    @Bean
    public Advice expressionAdvice() {
        ExpressionEvaluatingRequestHandlerAdvice advice = new ExpressionEvaluatingRequestHandlerAdvice();
        advice.setSuccessChannelName("success.input");
        //advice.setOnSuccessExpressionString("payload.delete() + ' was successful'");
        //advice.setOnSuccessExpressionString("inputMessage.headers['file_originalFile'].renameTo(new java.io.File(payload.absolutePath + '.success.to.send'))");
        //advice.setFailureChannelName("failure.input");
        advice.setOnFailureExpressionString("payload + ' was bad, with reason: ' + #exception.cause.message");
        advice.setTrapException(true);
        return advice;
    }

    /**
     * Creating a default FTP connection.
     * @return ftpSessionFactory
     */
    @Bean
    public SessionFactory<FTPFile> createNewFtpSessionFactory() {
        final DefaultFtpSessionFactory factory = new DefaultFtpSessionFactory();
        factory.setHost("bom-fs.bom.ei");
        factory.setUsername("bom-ftp");
        factory.setPort(21);
        factory.setPassword("spicysilk");
        return factory;
    }

    /**
     * Creating a metadata store to be used across the application flows to prevent reprocessing the file if it is already processed.
     * This will save the new file in a metadata table in the DB with the state of the report, so when a new copy comes with different date it will be processed only.
     * @return metadataStore
     */
    @Bean
    public ConcurrentMetadataStore metadataStore(final DataSource dataSource) {
        return new JdbcMetadataStore(dataSource);
    }

    /**
     * Success channel that will handle the AdviceMessage from the outbound adapter in method localToFtpFlow
     * and sends the inputMessage file_originalFile to FTP destination folder specified.
     * @return integration flow
     */

    @Bean
    public IntegrationFlow success(){
        return f -> f.transform("inputMessage.headers['file_originalFile']")
                .transform(e -> {
                    //getting the Branch code from the Input message and calling the correct factory based on it
                    delegatingSessionFactoryAuto().setThreadKey(e.toString().substring(0,3));
                    return e;
                })
                .handle(Ftp.outboundAdapter(delegatingSessionFactoryAuto(), FileExistsMode.REPLACE)
                        .useTemporaryFileName(true)
                        .autoCreateDirectory(true)
                        .fileNameGenerator(new FileNameGenerator() {
                            @Override
                            public String generateFileName(Message<?> message) {
                                return new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date()) + ".ip";
                            }
                        })
                        .remoteDirectory("/ftp/Customs/3ptylog/In/History/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())) // + dateFormat.format(date)
                        .get(),e -> e.advice(expressionAdviceForSuccess()));

    }

    /**
     * Second advice that will delete the original file from local after sending it to History folder at FTP branch.
     * Used in the second outboundAdapter in the success channel.
     * @return Advice
     * */

    @Bean
    public Advice expressionAdviceForSuccess() {
        ExpressionEvaluatingRequestHandlerAdvice advice = new ExpressionEvaluatingRequestHandlerAdvice();
        advice.setOnSuccessExpressionString("payload.delete() + ' was successful'");
        advice.setOnFailureExpressionString("payload + ' was bad, with reason: ' + #exception.cause.message");
        advice.setTrapException(true);
        return advice;
    }

    @Bean
    public DelegatingSessionFactory<FTPFile> delegatingSessionFactoryAuto(){

        SessionFactoryLocator<FTPFile> sff = createNewFtpSessionFactoryAndAddItToTheLocator();
        return new DelegatingSessionFactory<FTPFile>(sff);
    }

    @Bean
    public SessionFactoryLocator<FTPFile> createNewFtpSessionFactoryAndAddItToTheLocator(){

        this.defaultSessionFactoryLocator.addSessionFactory("BOM",createNewFtpSessionFactory());
        return this.defaultSessionFactoryLocator;
    }

}

package com.invoiceconverter.invoiceconverter.configuration;

import com.invoiceconverter.invoiceconverter.batchreaders.InvoiceProcessingItemReader;
import com.invoiceconverter.invoiceconverter.batchwriters.JpaIpItemWriter;
import com.invoiceconverter.invoiceconverter.model.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private EntityManagerFactory emf;

    public final String  START_LINE =  "AZ"; //Specifying the line to stop at when counting the lines to skip in the count method


    @Bean
    public Step ipStep() throws Exception{
        return stepBuilderFactory.get("ipStep")
                .<InvoiceProcessing,InvoiceProcessing>chunk(5)
                //.reader(reader(null,null))
                .reader(reader())
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    public Job ipUserJob() throws Exception {
        return jobBuilderFactory.get("ipJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())// pointing to the correct line where the reader should start reading in ipStep.
                .next(ipStep())
                //.flow(sampleStep)
                //.end()
                .build();
    }

    @Bean
    public Step step1() throws Exception {

        return stepBuilderFactory.get("calculateLinesToSkip")
                .tasklet((contribution, chunkContext) -> {

                   // int linesToSkip = 22;//count(null); do the math here
                    Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
                    String filePath = (String) jobParameters.get("file_path");
                    int linesToSkip = count(filePath);
                    chunkContext.getStepContext().getStepExecution().getJobExecution()
                            .getExecutionContext().put("linesToSkip", linesToSkip);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    /**
     * Creating a count method to count the lines that should be skipped before start reading and mapping the data
     *@return Integer
     */

    @StepScope
    public int count(@Value("#{jobParameters[file_path]}") String filePath) throws Exception {

        final FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        FlatFileItemReader<String> flatFileItemReader = new FlatFileItemReader();
        flatFileItemReader.setResource(fileSystemResource);

       File source = new File(filePath);

       Scanner scanner = new Scanner(source);
       int count = 0;

       while(scanner.hasNext()) {
           String x = scanner.next();
           String arr = x;
           count++;
           if (START_LINE.equals(arr)) {
               System.out.println("Lines to skip " + count);
               scanner.close();
               return count;
           }else{
               System.out.println("File does not have header AZ!!!");
               System.out.println("arr : " + arr);
           }

       }
       scanner.close();
       return count;
    }

    @Bean
    @StepScope
    public FlatFileItemReader readers(@Value("#{jobParameters[file_path]}") String filePath,@Value("#{jobExecutionContext['linesToSkip']}") Integer linesToSkip) throws Exception {

        final FileSystemResource fileSystemResource = new FileSystemResource(filePath);

        return new FlatFileItemReaderBuilder()
                .name("testItemReader")
                .resource(fileSystemResource)
                .linesToSkip(linesToSkip)
                .lineMapper(ipLineMapper())
                .build();
    }

    @Bean
    public InvoiceProcessingItemReader reader() throws Exception {

        InvoiceProcessingItemReader invoiceProcessingItemReader = new InvoiceProcessingItemReader();
        invoiceProcessingItemReader.setFieldSetReader(readers(null,null));

        return invoiceProcessingItemReader;

    }

    @Bean
    public ItemWriter<InvoiceProcessing> writer(){
        return new JpaIpItemWriter();
    }

    @Bean
    public JpaItemWriter jpaItemWriter(){
        JpaItemWriter writer = new JpaItemWriter();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    @Bean
    public PatternMatchingCompositeLineMapper ipLineMapper() throws Exception {

        PatternMatchingCompositeLineMapper mapper = new PatternMatchingCompositeLineMapper();

        Map<String, LineTokenizer> tokenizers = new HashMap<String, LineTokenizer>();
        tokenizers.put("A*",invoiceHeaderTokenizer() );
        tokenizers.put("B*",invoiceAdjustmentTokenizer() );
        tokenizers.put("D*",invoiceLineTokenizer() );
        tokenizers.put("E*",invoiceLineAdjustTokenizer() );
        tokenizers.put("G*",licensesVisasAdjustTokenizer());
        tokenizers.put("H*",classificationDetailTokenizer());
        tokenizers.put("I*",invoicePartyTokenizer());
        tokenizers.put("J*",declarationTokenizer());
        tokenizers.put("L*",associationOfInvoiceToDeclarationTokenizer());
        tokenizers.put("P*",classificationDetailSetTokenizer());
        tokenizers.put("Q*",freightItemRefTokenizer());
        tokenizers.put("R*",usDepartmentOfTransportationTokenizer());
        tokenizers.put("S*",usFederalCommunicationsCommissionTokenizer());
        tokenizers.put("R*",usFoodAndDrugAdminTokenizer());
        tokenizers.put("U*",priorNoticeFoodAndDrugAdminTokenizer());
        tokenizers.put("V*",genericFieldsTokenizer());
        tokenizers.put("W*",invoiceWarningsTokenizer());
        tokenizers.put("X*",sailingMessagesTokenizer());
        tokenizers.put("Y*",vendorOrderPointTokenizer());
        tokenizers.put("Z",lineDocumentTokenizer());
        tokenizers.put("PH*",packingListHeaderTokenizer());
        tokenizers.put("PL", packingListLineTokenizer());
        tokenizers.put("ZZ*", fileEndRecordTokenizer());

        mapper.setTokenizers(tokenizers);

        Map<String, FieldSetMapper> mappers = new HashMap<String, FieldSetMapper>();
        mappers.put("A*", invoiceHeaderFieldSetMapper());
        mappers.put("B*", invoiceAdjustFieldSetMapper());
        mappers.put("D*", invoiceLineFieldSetMapper());
        mappers.put("E*", invoiceLineAdjustFieldSetMapper());
        mappers.put("G*", licensesVisasFieldSetMapper());
        mappers.put("H*", classificationDetailFieldSetMapper());
        mappers.put("I*", invoicePartyFieldSetMapper());
        mappers.put("J*", declarationFieldSetMapper());
        mappers.put("L*", associationOfInvoiceToDeclarationFieldSetMapper());
        mappers.put("P*", classificationDetailSetFieldSetMapper());
        mappers.put("Q*", freightItemRefFieldSetMapper());
        mappers.put("R*", usDepartmentOfTransportationFieldSetMapper());
        mappers.put("S*", usFederalCommunicationsCommissionFieldSetMapper());
        mappers.put("R*", usFoodAndDrugAdminFieldSetMapper());
        mappers.put("U*", priorNoticeFoodAndDrugAdminFieldSetMapper());
        mappers.put("V*", genericFieldsFieldSetMapper());
        mappers.put("W*", invoiceWarningsFieldSetMapper());
        mappers.put("X*", sailingMessagesFieldSetMapper());
        mappers.put("Y*", vendorOrderPointFieldSetMapper());
        mappers.put("Z", lineDocumentFieldSetMapper());
        mappers.put("PH*", packingListHeaderFieldSetMapper());
        mappers.put("PL*", packingListLineFieldSetMapper());
        mappers.put("ZZ*", fileEndRecordFieldSetMapper());

        mapper.setFieldSetMappers(mappers);

        return mapper;

    }

    /**
     * Creating Tokenizer and Mapper for Invoice Header
     *@return InvoiceHeader object
     */

    @Bean
    public LineTokenizer invoiceHeaderTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "action", "invPoNo", "invDate", "sellerGci", "sellerCustRef", "buyerGci", "delivTermCode", "delivTermLoc", "invCurr", "expCountry", "destCountry","exportDate","buyerSellerR",
                "invType","invTotal","payTermType","payTerm","contractExchRate","destContStat","computedValStat","totalPcs","descOfGoods","dangGoods","na1","na2","na3","crRef","csRef","c4Ref","dpRef","dcRef","dvRef","foRef",
                "inRef","pdRef","pjRef","snRef","svRef","dtRef","znRef","authSender","authSenderTitle","dHeight","dWidth","dLenght","dUom","pkgQty","pkdType","grossWeight","grossWeightUom","netWeight","netWeightUom",
                "remittanceCurr","comment","buyerCustRef","pkgId","pkgIdStart","pkgIdEnd","comment1","comment2","comment3","comment4","isPo","firstSaleId","reserved","vendorCode","vendorOrderPoint"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<InvoiceHeader> invoiceHeaderFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<InvoiceHeader> mapper =
                new BeanWrapperFieldSetMapper<InvoiceHeader>();

        mapper.setPrototypeBeanName("invoiceHeader");
        mapper.afterPropertiesSet();
        return mapper;
    }


    @Bean
    @Scope("prototype")
    public InvoiceHeader invoiceHeader() {
        return new InvoiceHeader();
    }

    /**
     * Creating Tokenizer and Mapper for Invoice Adjustment
     *@return InvoiceAdjustment object
     */

    @Bean
    public LineTokenizer invoiceAdjustmentTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","adjustCateg","adjustAmount","amountType","currency","exchangeRate"
                ,"qty","dutiable","inclInLineValue","clientAdjustCatg","clientAdjustCatgDesc","notes"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<InvoiceAdjustment> invoiceAdjustFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<InvoiceAdjustment> mapper =
                new BeanWrapperFieldSetMapper<InvoiceAdjustment>();

        mapper.setPrototypeBeanName("invoiceAdjust");
        mapper.afterPropertiesSet();
        return mapper;
    }


    @Bean
    @Scope("prototype")
    public InvoiceAdjustment invoiceAdjustment() {
        return new InvoiceAdjustment();
    }


    /**
     * Creating Tokenizer and Mapper for Invoice Line
     *@return InvoiceLine object
     */

    @Bean
    public LineTokenizer invoiceLineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId","reserved", "buyerProdId","sellerProdId","descOfGoods","qty","qtyUom","unitValue"
                ,"lineValue","customerInvLineNo","eccn","na1","qtyOrdered","exportLiceRefNo","dangGoods","destCountry","salesOrdNo","salesOrdLineNo","poNo"
                ,"poLineNo","na2","productIdType","productIdOrdered","cs","c4","dp","dc","dv","fo","in","pd","pj","qc","sk","sn","sNo"
                ,"sv","st","dt","zn","vp","cr","grossWght","grossWghtUom","netWght","netWghtUom","dHeight","dWidth","dLenght","dUom",
                "pkgType","pkgQty","customsProcCode1","customsProcCode2","customsProcCode3","containerNo","containerType","make","model",
                "year","pkgId","pkgIdStart","pkgIdEnd","dfmCode","volume","volUom","softwoodLumbApplies","softwoodLumbExpChrgs",
                "softwoodLumbExpPrice","prodDetail","countryOfOrigin","prodIdA","prodIdA2","supplierManfId"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<InvoiceLine> invoiceLineFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<InvoiceLine> mapper =
                new BeanWrapperFieldSetMapper<InvoiceLine>();

        mapper.setPrototypeBeanName("invoiceLine");
        mapper.afterPropertiesSet();
        return mapper;
    }


    @Bean
    @Scope("prototype")
    public InvoiceLine invoiceLine() {
        return new InvoiceLine();
    }

    /**
     * Creating Tokenizer and Mapper for Invoice Line Adjust
     *@return Invoice Line Adjust object
     */

    @Bean
    public LineTokenizer invoiceLineAdjustTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","adjustCateg","adjustAmount","amountType","currency","exchangeRate"
                ,"qty","inclInLineValue","clientAdjustCatg","clientAdjustCatgDesc","notes"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<LineAdjustment> invoiceLineAdjustFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<LineAdjustment> mapper =
                new BeanWrapperFieldSetMapper<LineAdjustment>();

        mapper.setPrototypeBeanName("invoiceLineAdjust");
        mapper.afterPropertiesSet();
        return mapper;
    }


    @Bean
    @Scope("prototype")
    public LineAdjustment invoiceLineAdjust() {
        return new LineAdjustment();
    }

    /**
     * Creating Tokenizer and Mapper for Licenses Visas
     *@return LicensesVisas object
     */

    @Bean
    public LineTokenizer licensesVisasAdjustTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","type","refNo","startDate","endDate","na1"
                ,"na2","code"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<LineAdjustment> licensesVisasFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<LineAdjustment> mapper =
                new BeanWrapperFieldSetMapper<LineAdjustment>();

        mapper.setPrototypeBeanName("licensesVisas");
        mapper.afterPropertiesSet();
        return mapper;
    }


    @Bean
    @Scope("prototype")
    public LicensesVisas licensesVisas() {
        return new LicensesVisas();
    }


    /**
     * Creating Tokenizer and Mapper for Classification Details
     *@return LicensesVisas object
     */

    @Bean
    public LineTokenizer classificationDetailTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","descOfGoods","hsCode","qty","unit","countryOfOriga1","regionOfOrig","statisQty1","statisUom1","statisQty2","statisUom2","statisQty3","statisUom3"
                ,"unitValue","percentWholeSet","lineValue","dutyReducProg","manufactId","antiDumpingDutyNo","countervailingDutyNo","secondarySpi","tarrifConcessionNo","na","rulingType"
                ,"rulingNo","manufactRefNo","manufactRefId"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<ClassificationDetail> classificationDetailFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<ClassificationDetail> mapper =
                new BeanWrapperFieldSetMapper<ClassificationDetail>();

        mapper.setPrototypeBeanName("classificationDetail");
        mapper.afterPropertiesSet();
        return mapper;
    }


    @Bean
    @Scope("prototype")
    public ClassificationDetail classificationDetail() {
        return new ClassificationDetail();
    }

    /**
     * Creating Tokenizer and Mapper for Invoice Party
     *@return InvoiceParty object
     */

    @Bean
    public LineTokenizer invoicePartyTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","gci","customerRef","invoicePartyName","invoicePartyTaxId","addressLine1","addressLine2","city","state","postalCode","country","areaCode","phoneNo","extension"
                ,"primanyContPrefix","primanyContFirstName","primanyContLastName","role","addressLine3","addressLine4","addressLine5","addressLine6","invoicePartyTaxIdType","alternateId"
                ,"alternateIdType","isfAssociateId"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<InvoiceParty> invoicePartyFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<InvoiceParty> mapper =
                new BeanWrapperFieldSetMapper<InvoiceParty>();

        mapper.setPrototypeBeanName("invoiceParty");
        mapper.afterPropertiesSet();
        return mapper;
    }


    @Bean
    @Scope("prototype")
    public InvoiceParty invoiceParty() {
        return new InvoiceParty();
    }

    /**
     * Creating Tokenizer and Mapper for Declaration
     *@return Declaration object
     */

    @Bean
    public LineTokenizer declarationTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "action","declNo","declDate","refNo","etmsFileNo","hbNo","mbNo","cargoControlNo","internationalTransChargs","insurance","modeOfTransp","originCode","destCode"
                ,"voyFlightNo","voyFlightDate","airFrghtChrgDisc","portOfLand","portOfImpCode","arrivalPortImportDate","portOfDeclCode","arrivalPortDeclDate","lloydsCode","vesselName","declType"
                ,"entryProcdType","carrierCode","containerized","cargoLoc","itNumber","itDate","manifestQty","manifestUom","grossWght","grossWghtUom","customsPortCode","portUnladingCode","interTranspChargCurr"
                ,"insuranceCurr","volume","volumeUom","netWegiht","netWegihtUom","customsCateg","isfBillingOfLadingIssuer","isfCutOffDate","isuranceExchangeRate","transpExchangeRate","isfVendorId"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<Declaration> declarationFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<Declaration> mapper =
                new BeanWrapperFieldSetMapper<Declaration>();

        mapper.setPrototypeBeanName("declaration");
        mapper.afterPropertiesSet();
        return mapper;
    }


    @Bean
    @Scope("prototype")
    public Declaration declaration() {
        return new Declaration();
    }

    /**
     * Creating Tokenizer and Mapper for AssociationOfInvoiceToDeclaration
     *@return AssociationOfInvoiceToDeclaration object
     */

    @Bean
    public LineTokenizer associationOfInvoiceToDeclarationTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "action","invNo","invDate","sellerGci","sellerCustRef","buyerGci","refNo","buyerCustRef"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<AssociationOfInvoiceToDeclaration> associationOfInvoiceToDeclarationFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<AssociationOfInvoiceToDeclaration> mapper =
                new BeanWrapperFieldSetMapper<AssociationOfInvoiceToDeclaration>();

        mapper.setPrototypeBeanName("associationOfInvoiceToDeclaration");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public AssociationOfInvoiceToDeclaration associationOfInvoiceToDeclaration() {
        return new AssociationOfInvoiceToDeclaration();
    }

    /**
     * Creating Tokenizer and Mapper for Classification Detail Set
     *@return ClassificationDetailSet object
     */

    @Bean
    public LineTokenizer classificationDetailSetTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","setType"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<ClassificationDetailSet> classificationDetailSetFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<ClassificationDetailSet> mapper =
                new BeanWrapperFieldSetMapper<ClassificationDetailSet>();

        mapper.setPrototypeBeanName("classificationDetailSet");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public ClassificationDetailSet classificationDetailSet() {
        return new ClassificationDetailSet();
    }

    /**
     * Creating Tokenizer and Mapper for Freight Item Ref
     *@return FreightItemRef object
     */

    @Bean
    public LineTokenizer freightItemRefTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","type","number"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<FreightItemRef> freightItemRefFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<FreightItemRef> mapper =
                new BeanWrapperFieldSetMapper<FreightItemRef>();

        mapper.setPrototypeBeanName("freightItemRef");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public FreightItemRef freightItemRef() {
        return new FreightItemRef();
    }

    /**
     * Creating Tokenizer and Mapper for Freight Item Ref
     *@return FreightItemRef object
     */

    @Bean
    public LineTokenizer usDepartmentOfTransportationTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","boxNo","clarification","countryOfOrigin","tireManufactId","tireManufactName"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<UsDepartmentOfTransportation> usDepartmentOfTransportationFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<UsDepartmentOfTransportation> mapper =
                new BeanWrapperFieldSetMapper<UsDepartmentOfTransportation>();

        mapper.setPrototypeBeanName("usDepartmentOfTransportation");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public UsDepartmentOfTransportation usDepartmentOfTransportation() {
        return new UsDepartmentOfTransportation();
    }

    /**
     * Creating Tokenizer and Mapper for UsFederal Communications Commission
     *@return UsFederalCommunicationsCommission object
     */

    @Bean
    public LineTokenizer usFederalCommunicationsCommissionTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","importCond","model","tradeName","identifier","qty","qtyApproval","withHoldPubInspect"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<UsFederalCommunicationsCommission> usFederalCommunicationsCommissionFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<UsFederalCommunicationsCommission> mapper =
                new BeanWrapperFieldSetMapper<UsFederalCommunicationsCommission>();

        mapper.setPrototypeBeanName("usFederalCommunicationsCommission");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public UsFederalCommunicationsCommission usFederalCommunicationsCommission() {
        return new UsFederalCommunicationsCommission();
    }

    /**
     * Creating Tokenizer and Mapper for Us Food And Drug Admin
     *@return UsFoodAndDrugAdmin object
     */

    @Bean
    public LineTokenizer usFoodAndDrugAdminTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","supplierId","clarification","countryOfOrigin","manufactId","prodCode","value","stored","estalishmentbId","qty1","qty1Uom","qty2","qty2Uom","qty3","qty3Uom","qty4","qty4Uom","qty5","qty5Uom","qty6","qty6Uom","complianceCode1","complianceQualif1","complianceCode2","complianceQualif2","complianceCode3"
                ,"complianceQualif3","complianceCode4","complianceQualif4","complianceQualif5"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<UsFoodAndDrugAdmin> usFoodAndDrugAdminFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<UsFoodAndDrugAdmin> mapper =
                new BeanWrapperFieldSetMapper<UsFoodAndDrugAdmin>();

        mapper.setPrototypeBeanName("usFoodAndDrugAdmin");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public UsFoodAndDrugAdmin usFoodAndDrugAdmin() {
        return new UsFoodAndDrugAdmin();
    }


    /**
     * Creating Tokenizer and Mapper for Prior Notice Food And Drug Admin
     *@return PriorNoticeFoodAndDrugAdmin object
     */

    @Bean
    public LineTokenizer priorNoticeFoodAndDrugAdminTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","manufactRegNo","exempt","prodType","supplierCountry","supplierRegNo","contactName","contactPhone","submitterType","lastName","firstName","phoneNumber","fax","email","firmName","addr1","addr2","city","stateOrProv","zip","submitterCountry","ownerType","anticiPortOfArriv","anticiDateOfArriv","anticiTimeOfArriv","anticiPointOfCross"
                ,"modeOfTrans","scacIataCode","carrierCountry","carrierName","freightLoc","voyFlgt","bolAirBillNo"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<PriorNoticeFoodAndDrugAdmin> priorNoticeFoodAndDrugAdminFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<PriorNoticeFoodAndDrugAdmin> mapper =
                new BeanWrapperFieldSetMapper<PriorNoticeFoodAndDrugAdmin>();

        mapper.setPrototypeBeanName("priorNoticeFoodAndDrugAdmin");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public PriorNoticeFoodAndDrugAdmin priorNoticeFoodAndDrugAdmin() {
        return new PriorNoticeFoodAndDrugAdmin();
    }

    /**
     * Creating Tokenizer and Mapper for Generic Fields
     *@return GenericFields object
     */

    @Bean
    public LineTokenizer genericFieldsTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","code","ref","date","qty","units"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<GenericFields> genericFieldsFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<GenericFields> mapper =
                new BeanWrapperFieldSetMapper<GenericFields>();

        mapper.setPrototypeBeanName("genericFields");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public GenericFields genericFields() {
        return new GenericFields();
    }

    /**
     * Creating Tokenizer and Mapper for Invoice Warnings
     *@return InvoiceWarnings object
     */

    @Bean
    public LineTokenizer invoiceWarningsTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","message"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<InvoiceWarnings> invoiceWarningsFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<InvoiceWarnings> mapper =
                new BeanWrapperFieldSetMapper<InvoiceWarnings>();

        mapper.setPrototypeBeanName("invoiceWarnings");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public InvoiceWarnings invoiceWarnings() {
        return new InvoiceWarnings();
    }

    /**
     * Creating Tokenizer and Mapper for Sailing Messages
     *@return SailingMessages object
     */

    @Bean
    public LineTokenizer sailingMessagesTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","poNo","prodId","containerNo","masterbNo","vesselName"
                ,"carrier","grossWght","grossWghtUom","estimatedArrivPortOfUnlad"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<SailingMessages> sailingMessagesFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<SailingMessages> mapper =
                new BeanWrapperFieldSetMapper<SailingMessages>();

        mapper.setPrototypeBeanName("sailingMessages");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public SailingMessages sailingMessages() {
        return new SailingMessages();
    }

    /**
     * Creating Tokenizer and Mapper for Vendor Order Point
     *@return VendorOrderPoint object
     */

    @Bean
    public LineTokenizer vendorOrderPointTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","poNo","prodId","containerNo","masterbNo","vesselName"
                ,"carrier","grossWght","grossWghtUom","estimatedArrivPortOfUnlad"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<VendorOrderPoint> vendorOrderPointFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<VendorOrderPoint> mapper =
                new BeanWrapperFieldSetMapper<VendorOrderPoint>();

        mapper.setPrototypeBeanName("vendorOrderPoint");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public VendorOrderPoint vendorOrderPoint() {
        return new VendorOrderPoint();
    }


    /**
     * Creating Tokenizer and Mapper for Line Document
     *@return LineDocument object
     */

    @Bean
    public LineTokenizer lineDocumentTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","docType","docDesc","docRequired","hasImages"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<LineDocument> lineDocumentFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<LineDocument> mapper =
                new BeanWrapperFieldSetMapper<LineDocument>();

        mapper.setPrototypeBeanName("lineDocument");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public LineDocument lineDocument() {
        return new LineDocument();
    }

    /**
     * Creating Tokenizer and Mapper for Packing List Header
     *@return PackingListHeader object
     */

    @Bean
    public LineTokenizer packingListHeaderTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","packingListNo","packingListDate","totalCases","totalPcs","totalVol","totalVolUom","totalGrossWght"
                ,"totalGrossWghtUom","totalNetWght","totalNetWghtUom","comments"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<PackingListHeader> packingListHeaderFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<PackingListHeader> mapper =
                new BeanWrapperFieldSetMapper<PackingListHeader>();

        mapper.setPrototypeBeanName("packingListHeader");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public PackingListHeader packingListHeader() {
        return new PackingListHeader();
    }

    /**
     * Creating Tokenizer and Mapper for Packing List Line
     *@return PackingListLine object
     */

    @Bean
    public LineTokenizer packingListLineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId", "reserved","packingListNo","packingListDate","totalCases","totalPcs","totalVol","totalVolUom","totalGrossWght"
                ,"totalGrossWghtUom","totalNetWght","totalNetWghtUom","comments"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<PackingListLine> packingListLineFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<PackingListLine> mapper =
                new BeanWrapperFieldSetMapper<PackingListLine>();

        mapper.setPrototypeBeanName("packingListLine");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public PackingListLine packingListLine() {
        return new PackingListLine();
    }

    /**
     * Creating Tokenizer and Mapper for Packing List Header
     *@return PackingListHeader object
     */

    @Bean
    public LineTokenizer fileEndRecordTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
        tokenizer.setNames(new String[] { "recordId"});
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<FileEndRecord> fileEndRecordFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<FileEndRecord> mapper =
                new BeanWrapperFieldSetMapper<FileEndRecord>();

        mapper.setPrototypeBeanName("fileEndRecord");
        mapper.afterPropertiesSet();
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public FileEndRecord fileEndRecord() {
        return new FileEndRecord();
    }
}
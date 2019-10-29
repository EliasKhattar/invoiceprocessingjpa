package com.invoiceconverter.invoiceconverter.batchreaders;

import com.invoiceconverter.invoiceconverter.model.InvoiceHeader;
import com.invoiceconverter.invoiceconverter.model.InvoiceParty;
import com.invoiceconverter.invoiceconverter.model.InvoiceProcessing;
import lombok.Setter;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;

import java.util.ArrayList;

import static org.apache.logging.log4j.LogManager.getLogger;

public class InvoiceProcessingItemReader implements ItemReader<InvoiceProcessing>, ItemStream{

    private static final Logger LOG = getLogger(InvoiceProcessingItemReader.class);

    private InvoiceProcessing invoiceProcessing;

    private InvoiceHeader invoiceHeader;
    private FieldSetMapper<InvoiceHeader> invoiceHeaderMapper;
    private FieldSetMapper<InvoiceParty> invoicePartyMapper;

    private boolean recordFinished;

    private FlatFileItemReader<FieldSet> fieldSetReader;

    @Nullable
    @Override
    public InvoiceProcessing read() throws Exception {
        recordFinished = false;

        while (!recordFinished) {
            System.out.println( "Item reader : " + fieldSetReader.read());
            process(fieldSetReader.read());
        }

        LOG.info("Mapped : " + invoiceProcessing);

        InvoiceProcessing result = invoiceProcessing;
        invoiceProcessing = null;

        return result;
    }

    private void process(FieldSet fieldSet) throws BindException {

// finish processing if we hit the end of file

        System.out.println("Read : " + fieldSet);

        if (fieldSet == null) {
            LOG.debug("FINISHED");
            recordFinished = true;
            invoiceProcessing = null;
            return;
        }

        String lineId = fieldSet.readString(0);

        System.out.println("Line ID : " + lineId);

        if (fieldSet instanceof InvoiceHeader)
        {
            invoiceHeader = (InvoiceHeader) fieldSet;
            System.out.println("Invoice header : " + invoiceHeader);
        }

        /*if(lineId.equals("A") ){
            invoiceHeader = invoiceHeaderMapper.mapFieldSet(read);
        }
        else if(lineId.equals("I")){
            LOG.info("Mapping Invoice Party");
            if(invoiceHeader.getInvoiceParty() == null){
                invoiceHeader.setInvoiceParty(new ArrayList<>());
            }
            invoiceHeader.getInvoiceParty().add(invoicePartyMapper.mapFieldSet(read));
        }*/
    }

    public void setFieldSetReader(FlatFileItemReader<FieldSet> fieldSetReader) {
        this.fieldSetReader = fieldSetReader;
    }

    @Override
    public void close() throws ItemStreamException {
        this.fieldSetReader.close();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.fieldSetReader.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        this.fieldSetReader.update(executionContext);
    }


    public void setInvoiceHeaderMapper(FieldSetMapper<InvoiceHeader> invoiceHeaderMapper) {
        this.invoiceHeaderMapper = invoiceHeaderMapper;
    }

    public void setInvoicePartyMapper(FieldSetMapper<InvoiceParty> invoicePartyMapper) {
        this.invoicePartyMapper = invoicePartyMapper;
    }


}

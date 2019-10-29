package com.invoiceconverter.invoiceconverter.batchwriters;

import com.invoiceconverter.invoiceconverter.model.*;
import com.invoiceconverter.invoiceconverter.repository.*;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class JpaIpItemWriter implements ItemWriter<InvoiceProcessing> {

    @Autowired
    InvoiceHeaderRepository invoiceHeaderRepository;
    @Autowired
    InvoiceAdjustmentRepo invoiceAdjustmentRepo;
    @Autowired
    InvoiceLineRepo invoiceLineRepo;
    @Autowired
    LineAdjustmentRepo lineAdjustmentRepo;
    @Autowired
    LicensesVisaRepo licensesVisaRepo;
    @Autowired
    ClassificationDetailRepo classificationDetailRepo;
    @Autowired
    InvoicePartyRepo invoicePartyRepo;
    @Autowired
    DeclarationRepo declarationRepo;
    @Autowired
    AssociationOfInvoiceToDeclarationRepo associationOfInvoiceToDeclarationRepo;
    @Autowired
    ClassificationDetailSetRepo classificationDetailSetRepo;
    @Autowired
    FreightItemRefRepo freightItemRefRepo;
    @Autowired
    UsDepartmentOfTransportationRepo usDepartmentOfTransportationRepo;
    @Autowired
    UsFederalCommunicationsCommissionRepo usFederalCommunicationsCommissionRepo;
    @Autowired
    UsFoodAndDrugAdminRepo usFoodAndDrugAdminRepo;
    @Autowired
    GenericFieldsRepo genericFieldsRepo;
    @Autowired
    InvoiceWarningsRepo invoiceWarningsRepo;
    @Autowired
    SailingMessagesRepo sailingMessagesRepo;
    @Autowired
    VendorOrderPointRepo vendorOrderPointRepo;
    @Autowired
    LineDocumentRepo lineDocumentRepo;
    @Autowired
    PackingListHeaderRepo packingListHeaderRepo;
    @Autowired
    PackingListLineRepo packingListLineRepo;

    @Override
    public void write(List<? extends InvoiceProcessing> list) throws Exception {
        for(InvoiceProcessing invoiceProcessing : list) {
            if (invoiceProcessing instanceof InvoiceHeader) {
                System.out.println("Inserting Invoice header to the DB : " + invoiceProcessing.toString());
                invoiceHeaderRepository.save((InvoiceHeader) invoiceProcessing);
            }else if (invoiceProcessing instanceof InvoiceAdjustment){
                invoiceAdjustmentRepo.save((InvoiceAdjustment)invoiceProcessing);
                System.out.println("Inserting Invoice Adjustment to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof InvoiceLine){
                invoiceLineRepo.save((InvoiceLine)invoiceProcessing);
                System.out.println("Inserting Invoice Line to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof LineAdjustment){
                lineAdjustmentRepo.save((LineAdjustment) invoiceProcessing);
                System.out.println("Inserting Line Adjustment to the DB" + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof LicensesVisas){
                licensesVisaRepo.save((LicensesVisas) invoiceProcessing);
                System.out.println("Inserting Licenses and Visa to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof ClassificationDetail){
                classificationDetailRepo.save((ClassificationDetail) invoiceProcessing);
                System.out.println("Inserting Classification Detail to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof InvoiceParty){
                invoicePartyRepo.save((InvoiceParty)invoiceProcessing);
                System.out.println("Inserting Invoice Party to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof Declaration){
                declarationRepo.save((Declaration) invoiceProcessing);
                System.out.println("Inserting Declaration to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof AssociationOfInvoiceToDeclaration){
                associationOfInvoiceToDeclarationRepo.save((AssociationOfInvoiceToDeclaration) invoiceProcessing);
                System.out.println("Inserting Association Of Invoice To Declaration to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof ClassificationDetailSet){
                classificationDetailSetRepo.save((ClassificationDetailSet) invoiceProcessing);
                System.out.println("Inserting Classification Detail Set to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof FreightItemRef){
                freightItemRefRepo.save((FreightItemRef)invoiceProcessing);
                System.out.println("Inserting Freight Item Ref to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof UsDepartmentOfTransportation){
                usDepartmentOfTransportationRepo.save((UsDepartmentOfTransportation)invoiceProcessing);
                System.out.println("Inserting US Department Of Transportation to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof UsFederalCommunicationsCommission){
                usFederalCommunicationsCommissionRepo.save((UsFederalCommunicationsCommission) invoiceProcessing);
                System.out.println("Inserting US Federal Communications Commission to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof UsFoodAndDrugAdmin){
                usFoodAndDrugAdminRepo.save((UsFoodAndDrugAdmin)invoiceProcessing);
                System.out.println("Inserting US Food And Drug Admin to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof GenericFields){
                genericFieldsRepo.save((GenericFields)invoiceProcessing);
                System.out.println("Inserting Generic Fields to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof InvoiceWarnings){
                invoiceWarningsRepo.save((InvoiceWarnings)invoiceProcessing);
                System.out.println("Inserting Invoice Warnings to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof SailingMessages){
                sailingMessagesRepo.save((SailingMessages)invoiceProcessing);
                System.out.println("Inserting Sailing Messages to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof VendorOrderPoint){
                vendorOrderPointRepo.save((VendorOrderPoint)invoiceProcessing);
                System.out.println("Inserting Vendor Order Point to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof LineDocument){
                lineDocumentRepo.save((LineDocument)invoiceProcessing);
                System.out.println("Inserting Line Document to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof PackingListHeader){
                packingListHeaderRepo.save((PackingListHeader)invoiceProcessing);
                System.out.println("Inserting Packing List Header to the DB : " + invoiceProcessing.toString());
            }else if (invoiceProcessing instanceof PackingListLine){
                packingListLineRepo.save((PackingListLine)invoiceProcessing);
                System.out.println("Inserting Packing List Line to the DB : " + invoiceProcessing.toString());

            }
        }
    }
}

package com.invoiceconverter.invoiceconverter.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class AssociationOfInvoiceToDeclaration extends InvoiceProcessing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
    @Size(max = 1)
    private String action;
    @Size(max = 20)
    private String invNo;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private String invDate;
    @Size(max = 8)
    private String sellerGci;
    @Size(max = 35)
    private String sellerCustRef;
    @Size(max = 8)
    private String buyerGci;
    @Size(max = 35)
    private String refNo;
    @Size(max = 35)
    private String buyerCustRef;



}

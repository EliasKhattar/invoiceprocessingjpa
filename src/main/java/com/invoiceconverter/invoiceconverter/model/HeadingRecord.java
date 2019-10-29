package com.invoiceconverter.invoiceconverter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HeadingRecord extends InvoiceProcessing{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String returnEmail;
    private String NotReq;
    private String branchCode;
    private String system;
    private String impExpFlag;
    @Size(max = 8)
    private String buyerSellerGCI;
    @Size(max = 2)
    private String buyerSellerRole;
    @Email
    private String acknowEmail;
    private String specVersion;


}

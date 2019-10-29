package com.invoiceconverter.invoiceconverter.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class PriorNoticeFoodAndDrugAdmin extends InvoiceProcessing{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
    private String reserved;

    @Size(max = 35)
    private String manufactRegNo;
    @Size(max = 1)
    private String exempt;
    @Size(max = 1)
    private String prodType;
    @Size(max = 3)
    private String supplierCountry;
    @Size(max = 11)
    private String supplierRegNo;
    @Size(max = 35)
    private String contactName;
    @Size(max = 35)
    private String contactPhone;
    @Size(max = 1)
    private String submitterType;
    @Size(max = 35)
    private String lastName;
    @Size(max = 35)
    private String firstName;
    @Size(max = 35)
    private String phoneNumber;
    @Size(max = 35)
    private String fax;
    @Email
    private String email;
    @Size(max = 35)
    private String firmName;
    @Size(max = 35)
    private String addr1;
    @Size(max = 35)
    private String addr2;
    @Size(max = 35)
    private String city;
    @Size(max = 35)
    private String stateOrProv;
    @Size(max = 35)
    private String zip;
    @Size(max = 3)
    private String submitterCountry;
    @Size(max = 1)
    private String ownerType;
    @Size(max = 4)
    private String anticiPortOfArriv;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private String anticiDateOfArriv;
    @DateTimeFormat(pattern = "HH:MM")
    private String anticiTimeOfArriv;
    @Size(max = 35)
    private String anticiPointOfCross;
    @Size(max = 2)
    private String modeOfTrans;
    @Size(max = 4)
    private String scacIataCode;
    @Size(max = 3)
    private String carrierCountry;
    @Size(max = 35)
    private String carrierName;
    @Size(max = 35)
    private String freightLoc;
    @Size(max = 35)
    private String voyFlgt;
    @Size(max = 35)
    private String bolAirBillNo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private InvoiceHeader priorNoticeFoodAndDrugAdmins;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private UsFoodAndDrugAdmin priorNoticeFoodAndDrugAdmin;


}
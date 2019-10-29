package com.invoiceconverter.invoiceconverter.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class UsFoodAndDrugAdmin extends InvoiceProcessing{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
    private String reserved;

    @Size(max = 35)
    private String supplierId;
    @Size(max = 3)
    private String countryOfOrig;
    @Size(max = 35)
    private String manufactId;
    @Size(max = 7)
    private String prodCode;
    private Integer value;
    @Size(max = 1)
    private String stored;
    @Size(max = 35)
    private String estalishmentbId;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal qty1;
    @Size(max = 4)
    private String qty1Uom;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal qty2;
    @Size(max = 4)
    private String qty2Uom;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal qty3;
    @Size(max = 4)
    private String qty3Uom;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal qty4;
    @Size(max = 4)
    private String qty4Uom;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal qty5;
    @Size(max = 4)
    private String qty5Uom;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal qty6;
    @Size(max = 4)
    private String qty6Uom;
    @Size(max = 3)
    private String complianceCode1;
    @Size(max = 35)
    private String complianceQualif1;
    @Size(max = 3)
    private String complianceCode2;
    @Size(max = 35)
    private String complianceQualif2;
    @Size(max = 3)
    private String complianceCode3;
    @Size(max = 35)
    private String complianceQualif3;
    @Size(max = 3)
    private String complianceCode4;
    @Size(max = 35)
    private String complianceQualif4;
    @Size(max = 3)
    private String complianceCode5;
    @Size(max = 35)
    private String complianceQualif5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usFoodAndDrugAdmin_id")
    private ClassificationDetail usFoodAndDrugAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceLineFda_id")
    private InvoiceLine invoiceLineFda;

    @OneToOne(mappedBy = "priorNoticeFoodAndDrugAdmin", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private  PriorNoticeFoodAndDrugAdmin priorNoticeFoodAndDrugAdmin;

}

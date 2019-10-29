package com.invoiceconverter.invoiceconverter.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class ClassificationDetail extends InvoiceProcessing{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
    private String reserved;

    @Size(max = 70)
    private String descOfGoods;
    @Size(max = 35)
    private String hsCode;
    @Digits(integer = 18, fraction = 3)
    private BigDecimal qty;
    @Size(max = 4)
    private String unit;
    @Size(max = 3)
    private String countryOfOrig;
    @Size(max = 3)
    private String regionOfOrig;
    @Digits(integer = 18, fraction = 3)
    private BigDecimal statisQty1;
    @Size(max = 3)
    private String statisUom1;
    @Digits(integer = 18, fraction = 3)
    private BigDecimal statisQty2;
    @Size(max = 3)
    private String statisUom2;
    @Digits(integer = 18, fraction = 3)
    private BigDecimal statisQty3;
    @Size(max = 3)
    private String statisUom3;
    @Digits(integer = 15, fraction = 8)
    private BigDecimal unitValue;
    @Digits(integer = 11, fraction = 6)
    private BigDecimal percentWholeSet;
    @Digits(integer = 15, fraction = 8)
    private BigDecimal lineValue;
    @Size(max = 5)
    private String dutyReducProg;
    @Size(max = 35)
    private String manufactId;
    @Size(max = 35)
    private String antiDumpingDutyNo;
    @Size(max = 35)
    private String countervailingDutyNo;
    @Size(max = 1)
    private String secondarySpi;
    @Size(max = 35)
    private String tarrifConcessionNo;
    @Size(max = 3)
    private String na;
    @Size(max = 4)
    private String rulingType;
    @Size(max = 35)
    private String rulingNo;
    @Size(max = 35)
    private String manufactRefNo;
    @Size(max = 40)
    private String manufactRefId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "classificationDetail_id")
    private ClassificationDetailSet classificationDetail;

    @OneToMany(mappedBy = "lineAdjustment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineAdjustment> lineAdjustment = new ArrayList<>();

    @OneToMany(mappedBy = "licensesVisas", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LicensesVisas> licensesVisas = new ArrayList<>();

    @OneToMany(mappedBy = "usDepartmentOfTransportation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsDepartmentOfTransportation> usDepartmentOfTransportation = new ArrayList<>();

    @OneToMany(mappedBy = "usFederalCommunicationsCommission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsFederalCommunicationsCommission> usFederalCommunicationsCommission = new ArrayList<>();

    @OneToMany(mappedBy = "usFoodAndDrugAdmin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsFoodAndDrugAdmin> usFoodAndDrugAdmin = new ArrayList<>();

    @OneToMany(mappedBy = "lineDocument", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineDocument> lineDocument = new ArrayList<>();

    public void setStatisQty1(BigDecimal statisQty1) {
        if (statisQty1 == null)
            this.statisQty1 = BigDecimal.valueOf(0.0);
        else
        this.statisQty1 = statisQty1;
    }

    public void setStatisQty2(BigDecimal statisQty2) {
        if (statisQty2 == null)
            this.statisQty2 = BigDecimal.valueOf(0.0);
        else
        this.statisQty2 = statisQty2;
    }

    public void setStatisQty3(BigDecimal statisQty3) {
        if (statisQty3 == null)
            this.statisQty3 = BigDecimal.valueOf(0.0);
        else
        this.statisQty3 = statisQty3;
    }

    public void setPercentWholeSet(BigDecimal percentWholeSet) {
        if (percentWholeSet == null)
            this.percentWholeSet = BigDecimal.valueOf(0.0);
        else
        this.percentWholeSet = percentWholeSet;
    }


}

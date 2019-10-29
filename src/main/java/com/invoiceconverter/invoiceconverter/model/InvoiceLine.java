package com.invoiceconverter.invoiceconverter.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceLine extends InvoiceProcessing{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
    private String reserved;

    @Size(max = 35)
    private String buyerProdId;
    @Size(max = 35)
    private String sellerProdId;
    @Size(max = 1200)
    private String descOfGoods;
    @Digits(integer = 18, fraction = 3)
    private BigDecimal qty;
    @Size(max = 4)
    private String qtyUom;

    @Digits(integer = 15, fraction = 8)
    private BigDecimal unitValue;
    @Digits(integer = 15, fraction = 8)
    private BigDecimal lineValue;

    private Integer customerInvLineNo;
    @Size(max = 15)
    private String eccn;
    @Size(max = 1)
    private String na1;
    @Digits(integer = 18, fraction = 3)
    private BigDecimal qtyOrdered;
    @Size(max = 12)
    private String exportLiceRefNo;
    @Size(max = 1)
    private String dangGoods;
    @Size(max = 3)
    private String destCountry;
    @Size(max = 35)
    private String salesOrdNo;

    private Integer salesOrdLineNo;
    @Size(max = 35)
    private String poNo;

    private Integer poLineNo;
    @Size(max = 10)
    private String na2;
    @Size(max = 4)
    private String productIdType;
    @Size(max = 35)
    private String productIdOrdered;
    @Size(max = 35)
    private String cs;
    @Size(max = 35)
    private String c4;
    @Size(max = 35)
    private String dp;
    @Size(max = 35)
    private String dc;
    @Size(max = 35)
    private String dv;
    @Size(max = 35)
    private String fo;
    @Size(max = 35)
    private String in;
    @Size(max = 35)
    private String pd;
    @Size(max = 35)
    private String pj;
    @Size(max = 35)
    private String qc;
    @Size(max = 35)
    private String sk;
    @Size(max = 35)
    private String sn;//seal no
    @Size(max = 35)
    private String sNo; //serial No
    @Size(max = 35)
    private String sv;
    @Size(max = 35)
    private String st;
    @Size(max = 35)
    private String dt;
    @Size(max = 35)
    private String zn;
    @Size(max = 35)
    private String vp;
    @Size(max = 35)
    private String cr;
    @Digits(integer = 18, fraction = 3)
    private BigDecimal grossWght;
    @Size(max = 3)
    private String grossWghtUom;
    @Digits(integer = 18, fraction = 3)
    private BigDecimal netWght;
    @Size(max = 3)
    private String netWghtUom;
    @Digits(integer = 18, fraction = 3)
    private BigDecimal dHeight;
    @Digits(integer = 18, fraction = 3)
    private BigDecimal dWidth;
    @Digits(integer = 18, fraction = 3)
    private BigDecimal dLenght;
    @Size(max = 4)
    private String dUom;
    @Size(max = 4)
    private String pkgType;

    private Integer pkgQty;
    @Size(max = 2)
    private String customsProcCode1;
    @Size(max = 2)
    private String customsProcCode2;
    @Size(max = 6)
    private String customsProcCode3;
    @Size(max = 35)
    private String containerNo;
    @Size(max = 5)
    private String containerType;
    @Size(max = 35)
    private String make;
    @Size(max = 35)
    private String model;

    private Integer year;
    @Size(max = 35)
    private String pkgId;
    @Size(max = 35)
    private String pkgIdStart;
    @Size(max = 35)
    private String pkgIdEnd;
    @Size(max = 1)
    private String dfmCode;
    @Digits(integer = 18, fraction = 3)
    private BigDecimal volume;
    @Size(max = 3)
    private String volUom;
    @Size(max = 1)
    private String softwoodLumbApplies;
    @Digits(integer = 12, fraction = 2)
    private BigDecimal softwoodLumbExpChrgs;
    @Digits(integer = 12, fraction = 2)
    private BigDecimal softwoodLumbExpPrice;
    @Size(max = 12000)
    private String prodDetail;
    @Size(max = 3)
    private String countryOfOrigin;
    @Size(max = 120)
    private String prodIdA;
    @Size(max = 120)
    private String prodIdA2;
    @Size(max = 35)
    private String supplierManfId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceLine_id")
    private InvoiceHeader invoiceLine;

    @OneToMany(mappedBy = "freightItemRef", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FreightItemRef> freightItemRef = new ArrayList<>();

    @OneToMany(mappedBy = "genericFieldsInvoiceLine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GenericFields> genericFieldsInvoiceLine = new ArrayList<>();

    @OneToMany(mappedBy = "invoiceWarningsInvoiceLine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceWarnings> invoiceWarningsInvoiceLine = new ArrayList<>();

    @OneToOne(mappedBy = "classificationDetailSet", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private ClassificationDetailSet classificationDetailSet;

    @OneToMany(mappedBy = "invoiceLineAdjustment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineAdjustment> invoiceLineAdjustment = new ArrayList<>();

    @OneToMany(mappedBy = "invoiceLineLicensesVisas", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LicensesVisas> invoiceLineLicensesVisas = new ArrayList<>();

    @OneToMany(mappedBy = "invoiceLineDop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsDepartmentOfTransportation> invoiceLineDop = new ArrayList<>();

    @OneToMany(mappedBy = "invoiceLineFcc", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsFederalCommunicationsCommission> invoiceLineFcc  = new ArrayList<>();

    @OneToMany(mappedBy = "invoiceLineFda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsFoodAndDrugAdmin> invoiceLineFda   = new ArrayList<>();

    @OneToMany(mappedBy = "invoicelineDocument", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineDocument>  invoicelineDocument  = new ArrayList<>();

    public void setCustomerInvLineNo(Integer customerInvLineNo) {
        if(customerInvLineNo == null)
            this.customerInvLineNo = 0;
        else
        this.customerInvLineNo = customerInvLineNo;
    }

    public void setQtyOrdered(BigDecimal qtyOrdered) {
        if (qtyOrdered == null)
            this.qtyOrdered = BigDecimal.valueOf(0.0);
        else
        this.qtyOrdered = qtyOrdered;
    }

    public void setSalesOrdLineNo(Integer salesOrdLineNo) {
        if(salesOrdLineNo == null)
            this.salesOrdLineNo = 0;
        else
        this.salesOrdLineNo = salesOrdLineNo;
    }

    public void setPoLineNo(Integer poLineNo) {
        if(poLineNo == null)
            this.poLineNo = 0;
        else
        this.poLineNo = poLineNo;
    }

    public void setGrossWght(BigDecimal grossWght) {
        if (grossWght == null)
            this.grossWght = BigDecimal.valueOf(0.0);
        else
        this.grossWght = grossWght;
    }

    public void setNetWght(BigDecimal netWght) {
        if (netWght == null)
            this.netWght = BigDecimal.valueOf(0.0);
        else
        this.netWght = netWght;
    }

    public void setdHeight(BigDecimal dHeight) {
        if (dHeight == null)
            this.dHeight = BigDecimal.valueOf(0.0);
        else
        this.dHeight = dHeight;
    }

    public void setdWidth(BigDecimal dWidth) {
        if (dWidth == null)
            this.dWidth = BigDecimal.valueOf(0.0);
        else
        this.dWidth = dWidth;
    }

    public void setdLenght(BigDecimal dLenght) {
        if (dLenght == null)
            this.dLenght = BigDecimal.valueOf(0.0);
        else
        this.dLenght = dLenght;
    }

    public void setYear(Integer year) {
        if (year == null)
            this.year = 0;
        else
        this.year = year;
    }
}

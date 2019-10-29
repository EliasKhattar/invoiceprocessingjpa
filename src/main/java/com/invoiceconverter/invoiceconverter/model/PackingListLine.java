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
public class PackingListLine extends InvoiceProcessing{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
    private String reserved;

    @Size(max = 20)
    private String prodId;
    @Size(max = 80)
    private String prodDesc;
    @Size(max = 20)
    private String poNo;
    @Size(max = 20)
    private String packageIdStart;
    @Size(max = 20)
    private String packageIdEnd;
    @Digits(integer = 18, fraction = 6)
    private BigDecimal noOfCases;
    @Digits(integer = 18, fraction = 6)
    private BigDecimal pcsPerCase;
    @Digits(integer = 18, fraction = 6)
    private BigDecimal qty;
    @Size(max = 12)
    private String qtyUom;
    @Digits(integer = 18, fraction = 6)
    private BigDecimal grossWght;
    @Size(max = 12)
    private String grossWghtUom;
    @Digits(integer = 18, fraction = 6)
    private BigDecimal netWght;
    @Size(max = 12)
    private String netWghtUom;
    @Digits(integer = 18, fraction = 6)
    private BigDecimal vol;
    @Size(max = 12)
    private String volUom;
    @Digits(integer = 18, fraction = 6)
    private BigDecimal dLenght;
    @Digits(integer = 18, fraction = 6)
    private BigDecimal dWidth;
    @Digits(integer = 18, fraction = 6)
    private BigDecimal dHeight;
    @Size(max = 12)
    private String dUom;
    @Size(max = 500)
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packingListLine_id")
    private PackingListHeader packingListLine;

}

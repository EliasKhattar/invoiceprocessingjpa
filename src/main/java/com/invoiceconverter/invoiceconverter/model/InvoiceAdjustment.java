package com.invoiceconverter.invoiceconverter.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceAdjustment extends InvoiceProcessing{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
    private String reserved;

    @Size(max = 6)
    private String adjustCateg;
    @Digits(integer = 15, fraction = 8)
    private BigDecimal adjustAmount;
    @Size(max = 1)
    private String amountType;
    @Size(max = 3)
    private String currency;
    @Digits(integer = 17, fraction = 9)
    private BigDecimal exchangeRate;

    private Integer qty;
    @Size(max = 1)
    private String dutiable;
    @Size(max = 1)
    private String inclInLineValue;
    @Size(max = 35)
    private String clientAdjustCatg;
    @Size(max = 70)
    private String clientAdjustCatgDesc;
    @Size(max = 80)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceHeader_id")
    private InvoiceHeader invoiceAdjustments;


    public void setExchangeRate(BigDecimal exchangeRate) {
        if (exchangeRate == null)
            this.exchangeRate = BigDecimal.valueOf(0.0);
        else
            this.exchangeRate = exchangeRate;
    }

    public void setQty(Integer qty) {
        if(qty == null)
            this.qty = 0;
        else
            this.qty = qty;
    }

}

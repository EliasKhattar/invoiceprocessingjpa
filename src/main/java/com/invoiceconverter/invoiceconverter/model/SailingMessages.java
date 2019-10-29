package com.invoiceconverter.invoiceconverter.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class SailingMessages extends InvoiceProcessing{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
    private String reserved;

    @Size(max = 35)
    private String poNo;
    @Size(max = 35)
    private String prodId;
    @Size(max = 35)
    private String containerNo;
    @Size(max = 35)
    private String masterbNo;
    @Size(max = 35)
    private String vesselName;
    @Size(max = 4)
    private String carrier;
    @Digits(integer = 18, fraction = 3)
    private BigDecimal grossWght;
    @Size(max = 4)
    private String grossWghtUom;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private String estimatedArrivPortOfUnlad;

}

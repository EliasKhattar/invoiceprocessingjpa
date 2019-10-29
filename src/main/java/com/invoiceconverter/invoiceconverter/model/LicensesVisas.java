package com.invoiceconverter.invoiceconverter.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class LicensesVisas extends InvoiceProcessing{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
    private String reserved;

    @Size(max = 4)
    private String type;
    @Size(max = 35)
    private String refNo;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private String startDate;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private String endDate;
    @Digits(integer = 18, fraction = 3)
    private BigDecimal na1;
    @Size(max = 4)
    private String na2;
    @Size(max = 20)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "licensesVisas_id")
    private ClassificationDetail licensesVisas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceLineLicensesVisas_id")
    private InvoiceLine invoiceLineLicensesVisas;

}

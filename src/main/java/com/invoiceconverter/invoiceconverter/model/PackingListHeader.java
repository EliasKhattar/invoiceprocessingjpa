package com.invoiceconverter.invoiceconverter.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
public class PackingListHeader extends InvoiceProcessing{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
    private String reserved;

    @Size(max = 20)
    private String packingListNo;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private String packingListDate;
    @Digits(integer = 18, fraction = 6)
    private BigDecimal totalCases;
    @Digits(integer = 18, fraction = 6)
    private BigDecimal totalPcs;
    @Digits(integer = 18, fraction = 6)
    private BigDecimal totalVol;
    @Size(max = 12)
    private String totalVolUom;
    @Digits(integer = 18, fraction = 6)
    private BigDecimal totalGrossWght;
    @Size(max = 12)
    private String totalGrossWghtUom;
    @Digits(integer = 18, fraction = 6)
    private BigDecimal totalNetWght;
    @Size(max = 12)
    private String totalNetWghtUom;
    @Size(max = 5000)
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packingListHeader")
    private InvoiceHeader packingListHeader;

    @OneToMany(mappedBy = "packingListLine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackingListLine> packingListLine = new ArrayList<>();

}

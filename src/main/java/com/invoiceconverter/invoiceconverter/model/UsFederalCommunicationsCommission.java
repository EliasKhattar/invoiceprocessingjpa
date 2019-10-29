package com.invoiceconverter.invoiceconverter.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class UsFederalCommunicationsCommission extends InvoiceProcessing{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
    private String reserved;

    @Size(max = 2)
    private String importCond;
    @Size(max = 35)
    private String model;
    @Size(max = 35)
    private String tradeName;
    @Size(max = 35)
    private String identifier;

    private Integer qty;
    @Size(max = 1)
    private String qtyApproval;
    @Size(max = 1)
    private String withHoldPubInspect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usFederalCommunicationsCommission_id")
    private ClassificationDetail usFederalCommunicationsCommission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceLineFcc_id")
    private InvoiceLine invoiceLineFcc;



}

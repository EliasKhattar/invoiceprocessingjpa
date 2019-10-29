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
public class FreightItemRef extends InvoiceProcessing{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String recordId;
    private String reserved;

    @Size(max = 4)
    private String type;
    @Size(max = 35)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freightItemRef_id")
    private InvoiceLine freightItemRef;

}

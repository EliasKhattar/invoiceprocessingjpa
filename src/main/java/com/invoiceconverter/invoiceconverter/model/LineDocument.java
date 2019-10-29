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
public class LineDocument extends InvoiceProcessing{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
    private String reserved;

    @Size(max = 4)
    private String docType;
    @Size(max = 70)
    private String docDesc;
    @Size(max = 1)
    private String docRequired;
    @Size(max = 1)
    private String hasImages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lineDocument_id")
    private ClassificationDetail lineDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoicelineDocument_id")
    private InvoiceLine invoicelineDocument;

}

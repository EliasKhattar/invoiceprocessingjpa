package com.invoiceconverter.invoiceconverter.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Null;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class InvoiceWarnings extends InvoiceProcessing{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String recordId;
    private String reserved;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceWarnings_id")
    private InvoiceHeader invoiceWarnings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceWarningsLineInvoice_id")
    private InvoiceLine invoiceWarningsInvoiceLine;

}

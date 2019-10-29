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
public class UsDepartmentOfTransportation extends InvoiceProcessing{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
    private String reserved;

    @Size(max = 2)
    private String boxNo;
    @Size(max = 1)
    private String clarification;
    @Size(max = 3)
    private String countryOfOrigin;
    @Size(max = 4)
    private String tireManufactId;
    @Size(max = 35)
    private String tireManufactName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usDepartmentOfTransportation_id")
    private ClassificationDetail usDepartmentOfTransportation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceLineDop_id")
    private InvoiceLine invoiceLineDop;

}

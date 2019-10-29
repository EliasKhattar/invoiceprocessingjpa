package com.invoiceconverter.invoiceconverter.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class VendorOrderPoint extends InvoiceProcessing{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
    private String reserved;

    @Size(max = 35)
    private String poNo;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private String poDate;
    @Size(max = 40)
    private String vendorCode;
    @Size(max = 2)
    private String vendorOrderPoint;
}

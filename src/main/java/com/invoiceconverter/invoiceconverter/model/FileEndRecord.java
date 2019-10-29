package com.invoiceconverter.invoiceconverter.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class FileEndRecord extends InvoiceProcessing{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String recordId;
}

package com.invoiceconverter.invoiceconverter.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class ClassificationDetailSet extends InvoiceProcessing{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String recordId;
    private String reserved;

    @Size(max = 4)
    private String setType;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private InvoiceLine classificationDetailSet;

    @OneToMany(mappedBy = "classificationDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassificationDetail> classificationDetail = new ArrayList<>();

}

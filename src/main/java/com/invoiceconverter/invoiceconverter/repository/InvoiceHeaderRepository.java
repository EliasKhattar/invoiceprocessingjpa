package com.invoiceconverter.invoiceconverter.repository;

import com.invoiceconverter.invoiceconverter.model.InvoiceHeader;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceHeaderRepository extends CrudRepository<InvoiceHeader, Long> {
}

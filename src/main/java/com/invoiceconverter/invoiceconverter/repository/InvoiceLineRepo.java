package com.invoiceconverter.invoiceconverter.repository;

import com.invoiceconverter.invoiceconverter.model.InvoiceLine;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceLineRepo extends CrudRepository<InvoiceLine,Long> {
}

package com.invoiceconverter.invoiceconverter.repository;

import com.invoiceconverter.invoiceconverter.model.InvoiceWarnings;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceWarningsRepo extends CrudRepository<InvoiceWarnings, Long> {
}

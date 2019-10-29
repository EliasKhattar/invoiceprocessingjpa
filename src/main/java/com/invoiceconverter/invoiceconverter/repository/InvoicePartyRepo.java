package com.invoiceconverter.invoiceconverter.repository;

import com.invoiceconverter.invoiceconverter.model.InvoiceParty;
import org.springframework.data.repository.CrudRepository;

public interface InvoicePartyRepo extends CrudRepository<InvoiceParty, Long> {
}

package com.invoiceconverter.invoiceconverter.repository;

import com.invoiceconverter.invoiceconverter.model.InvoiceAdjustment;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceAdjustmentRepo extends CrudRepository<InvoiceAdjustment, Long> {
}

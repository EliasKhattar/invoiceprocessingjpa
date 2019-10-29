package com.invoiceconverter.invoiceconverter.repository;

import com.invoiceconverter.invoiceconverter.model.LineDocument;
import org.springframework.data.repository.CrudRepository;

public interface LineDocumentRepo extends CrudRepository<LineDocument, Long> {
}

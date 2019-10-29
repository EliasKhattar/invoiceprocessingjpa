package com.invoiceconverter.invoiceconverter.repository;

import com.invoiceconverter.invoiceconverter.model.Declaration;
import org.springframework.data.repository.CrudRepository;

public interface DeclarationRepo extends CrudRepository<Declaration, Long> {
}

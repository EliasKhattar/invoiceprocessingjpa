package com.invoiceconverter.invoiceconverter.repository;

import com.invoiceconverter.invoiceconverter.model.PackingListLine;
import org.springframework.data.repository.CrudRepository;

public interface PackingListLineRepo extends CrudRepository<PackingListLine, Long> {
}

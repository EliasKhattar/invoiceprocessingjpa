package com.invoiceconverter.invoiceconverter.repository;

import com.invoiceconverter.invoiceconverter.model.LineAdjustment;
import org.springframework.data.repository.CrudRepository;

public interface LineAdjustmentRepo extends CrudRepository<LineAdjustment, Long> {
}

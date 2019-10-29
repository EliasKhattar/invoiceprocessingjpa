package com.invoiceconverter.invoiceconverter.repository;

import com.invoiceconverter.invoiceconverter.model.VendorOrderPoint;
import org.springframework.data.repository.CrudRepository;

public interface VendorOrderPointRepo extends CrudRepository<VendorOrderPoint, Long> {
}

package com.invoiceconverter.invoiceconverter.services;

import com.invoiceconverter.invoiceconverter.command.BranchForm;
import com.invoiceconverter.invoiceconverter.model.Branch;

import java.util.List;

public interface BranchService {
    List<Branch> listAll();

    Branch getById(Long id);

    Branch saveOrUpdate(Branch branch);

    void delete(Long id);

    Branch saveOrUpdateBranchForm(BranchForm branchForm);

    boolean isAvailable(Long id);
}

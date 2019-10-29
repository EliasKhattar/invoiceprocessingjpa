package com.invoiceconverter.invoiceconverter.services;

import com.invoiceconverter.invoiceconverter.command.BranchForm;
import com.invoiceconverter.invoiceconverter.converters.BranchFormToBranch;
import com.invoiceconverter.invoiceconverter.model.Branch;
import com.invoiceconverter.invoiceconverter.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BranchServiceImp implements BranchService{

    private BranchRepository branchRepository;
    private BranchFormToBranch branchFormToBranch;

    @Autowired
    public BranchServiceImp(BranchRepository branchRepository, BranchFormToBranch branchFormToBranch) {

        this.branchRepository = branchRepository;
        this.branchFormToBranch = branchFormToBranch;
    }

    @Override
    public List<Branch> listAll() {
        List<Branch> branches = new ArrayList<>();
        branchRepository.findAll().forEach(branches::add);
        return branches;
    }

    @Override
    public Branch getById(Long id) {
        return branchRepository.findById(id).orElse(null);
    }

    @Override
    public Branch saveOrUpdate(Branch branch) {
        branchRepository.save(branch);
        return branch;
    }

    @Override
    public void delete(Long id) {
        branchRepository.deleteById(id);
    }

    @Override
    public Branch saveOrUpdateBranchForm(BranchForm branchForm) {

        Branch saveBranch = saveOrUpdate(branchFormToBranch.convert(branchForm));

        System.out.println("Saved Branch : " + saveBranch.getBranchCode());
        return saveBranch;

    }

    @Override
    public boolean isAvailable(Long id) {

        if(branchRepository.existsById(id))return true;

        return false;
    }

}

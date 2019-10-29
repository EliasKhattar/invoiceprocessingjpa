package com.invoiceconverter.invoiceconverter.converters;

import com.invoiceconverter.invoiceconverter.command.BranchForm;
import com.invoiceconverter.invoiceconverter.model.Branch;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BranchToBranchForm implements Converter<Branch, BranchForm> {


    @Override
    public BranchForm convert(Branch branch) {
        BranchForm branchForm = new BranchForm();
        branchForm.setBranchCode(branch.getBranchCode());
        branchForm.setHost(branch.getHost());
        branchForm.setFtpPort(branch.getFtpPort());
        branchForm.setId(branch.getId());
        branchForm.setPassword(branch.getPassword());
        branchForm.setUsern(branch.getUsern());
        branchForm.setFolderPathIn(branch.getFolderPathIn());
        branchForm.setFolderPathOut(branch.getFolderPathOut());

        return branchForm;
    }
}


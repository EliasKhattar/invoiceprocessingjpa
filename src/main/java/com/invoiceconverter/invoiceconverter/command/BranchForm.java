package com.invoiceconverter.invoiceconverter.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BranchForm {
    private Long id;

    @Size(min = 3, max = 3, message = "Branch code must be 3 characters")
    private String branchCode;

    @Size(min = 7, max = 7, message = "Username should be 7 characters (ftp-bbb)")
    private String usern;

    @NotNull(message = "Password Should not be null, Please enter password")
    @Size(min = 2, message = "Password should be at least 2 characters")
    private String password;

    @NotBlank(message = "Host should not be null, please enter correct host")
    @Size(min = 2, message = "Host should be at least 2 characters")
    private String host;

    @NotNull(message = "Port should not be null")
    @Min(value = 2, message = "Port should be at least 2 characters")
    private Integer ftpPort;

    @NotBlank(message = "Folder Path should not be null")
    private String folderPathIn;
    @NotBlank(message = "Folder Path should not be null")
    private String folderPathOut;


}

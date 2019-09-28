package com.auditlog.auditlog.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class OperationPerformedDto implements Serializable {

    @NotBlank
    private String customerLogin;
    @NotBlank
    private String customerName;
    @NotBlank
    private String date;
    @NotBlank
    private String operationCode;
    @NotBlank
    private String operationDescription;
}

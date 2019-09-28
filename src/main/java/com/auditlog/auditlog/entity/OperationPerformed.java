package com.auditlog.auditlog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("operationPerformed")
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("PMD.ShortVariable")
public class OperationPerformed {

    @Transient
    public static final String SEQUENCE_NAME = "operationPerformed_sequence";

    @Id
    private long id;
    private String customerLogin;
    private String customerName;
    private String date;
    private String operationCode;
    private String operationDescription;

}

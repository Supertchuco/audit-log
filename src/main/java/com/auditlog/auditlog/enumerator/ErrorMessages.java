package com.auditlog.auditlog.enumerator;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum to define error messages.
 */
@AllArgsConstructor
@Getter
public enum ErrorMessages {

    UNEXPECTED_ERROR("Unexpected error"),
    INVALID_DATE_FORMAT("Invalid date format"),
    INVALID_PAYLOAD("Invalid payload data"),
    OPERATION_PERFORMED_ALREADY_EXIST_ON_DATABASE("Operation performed already exist on database"),
    OPERATION_PERFORMED_NOT_FOUND("Operation performed not foundo on database");

    private String message;
}

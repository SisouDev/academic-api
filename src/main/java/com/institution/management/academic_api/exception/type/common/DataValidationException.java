package com.institution.management.academic_api.exception.type.common;

public class DataValidationException extends InvalidRequestException {
    public DataValidationException(String message) {
        super(message);
    }
}
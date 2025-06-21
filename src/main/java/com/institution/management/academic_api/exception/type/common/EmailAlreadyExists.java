package com.institution.management.academic_api.exception.type.common;

public class EmailAlreadyExists extends ConflictException {
    public EmailAlreadyExists(String message) {
        super(message);
    }
}

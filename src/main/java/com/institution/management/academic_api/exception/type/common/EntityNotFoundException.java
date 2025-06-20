package com.institution.management.academic_api.exception.type.common;

public class EntityNotFoundException extends ResourceNotFoundException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
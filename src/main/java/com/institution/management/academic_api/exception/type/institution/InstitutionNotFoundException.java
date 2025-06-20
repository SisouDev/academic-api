package com.institution.management.academic_api.exception.type.institution;

import com.institution.management.academic_api.exception.type.common.ResourceNotFoundException;

public class InstitutionNotFoundException extends ResourceNotFoundException {
    public InstitutionNotFoundException(String message) {
        super(message);
    }
}
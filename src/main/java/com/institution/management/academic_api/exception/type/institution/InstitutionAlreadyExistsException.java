package com.institution.management.academic_api.exception.type.institution;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class InstitutionAlreadyExistsException extends ConflictException {
    public InstitutionAlreadyExistsException(String message) {
        super(message);
    }
}

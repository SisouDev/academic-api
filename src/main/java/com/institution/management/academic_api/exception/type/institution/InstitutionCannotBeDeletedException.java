package com.institution.management.academic_api.exception.type.institution;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class InstitutionCannotBeDeletedException extends ConflictException {
    public InstitutionCannotBeDeletedException(String message) {
        super(message);
    }
}

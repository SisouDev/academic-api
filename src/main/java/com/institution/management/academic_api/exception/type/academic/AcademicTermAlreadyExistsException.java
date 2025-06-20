package com.institution.management.academic_api.exception.type.academic;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class AcademicTermAlreadyExistsException extends ConflictException {
    public AcademicTermAlreadyExistsException(String message) {
        super(message);
    }
}
package com.institution.management.academic_api.exception.type.student;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class AssessmentAlreadyExistsException extends ConflictException {
    public AssessmentAlreadyExistsException(String message) {
        super(message);
    }
}
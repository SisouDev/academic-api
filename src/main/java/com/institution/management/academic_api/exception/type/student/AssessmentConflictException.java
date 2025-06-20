package com.institution.management.academic_api.exception.type.student;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class AssessmentConflictException extends ConflictException {
    public AssessmentConflictException(String message) {
        super(message);
    }
}
package com.institution.management.academic_api.exception.type.student;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class EnrollmentConflictException extends ConflictException {
    public EnrollmentConflictException(String message) {
        super(message);
    }
}
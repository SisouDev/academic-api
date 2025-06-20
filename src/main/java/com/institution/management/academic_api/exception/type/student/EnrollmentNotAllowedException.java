package com.institution.management.academic_api.exception.type.student;

import com.institution.management.academic_api.exception.type.common.InvalidRequestException;

public class EnrollmentNotAllowedException extends InvalidRequestException {
    public EnrollmentNotAllowedException(String message) {
        super(message);
    }
}
package com.institution.management.academic_api.exception.type.student;

import com.institution.management.academic_api.exception.type.common.ResourceNotFoundException;

public class EnrollmentNotFoundException extends ResourceNotFoundException {
    public EnrollmentNotFoundException(String message) {
        super(message);
    }
}
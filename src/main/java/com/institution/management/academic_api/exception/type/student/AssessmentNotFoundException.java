package com.institution.management.academic_api.exception.type.student;

import com.institution.management.academic_api.exception.type.common.ResourceNotFoundException;

public class AssessmentNotFoundException extends ResourceNotFoundException {
    public AssessmentNotFoundException(String message) {
        super(message);
    }
}

package com.institution.management.academic_api.exception.type.course;

import com.institution.management.academic_api.exception.type.common.ResourceNotFoundException;

public class SubjectNotFoundException extends ResourceNotFoundException {
    public SubjectNotFoundException(String message) {
        super(message);
    }
}
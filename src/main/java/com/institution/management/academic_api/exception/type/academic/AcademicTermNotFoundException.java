package com.institution.management.academic_api.exception.type.academic;

import com.institution.management.academic_api.exception.type.common.ResourceNotFoundException;

public class AcademicTermNotFoundException extends ResourceNotFoundException {
    public AcademicTermNotFoundException(String message) {
        super(message);
    }
}
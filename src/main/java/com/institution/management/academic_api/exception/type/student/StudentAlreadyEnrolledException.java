package com.institution.management.academic_api.exception.type.student;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class StudentAlreadyEnrolledException extends ConflictException {
    public StudentAlreadyEnrolledException(String message) {
        super(message);
    }
}

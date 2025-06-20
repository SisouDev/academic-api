package com.institution.management.academic_api.exception.type.student;

import com.institution.management.academic_api.exception.type.common.InvalidRequestException;

public class StudentInactiveException extends InvalidRequestException {
    public StudentInactiveException(String message) {
        super(message);
    }
}
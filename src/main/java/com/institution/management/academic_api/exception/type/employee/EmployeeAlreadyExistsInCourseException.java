package com.institution.management.academic_api.exception.type.employee;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class EmployeeAlreadyExistsInCourseException extends ConflictException {
    public EmployeeAlreadyExistsInCourseException(String message) {
        super(message);
    }
}

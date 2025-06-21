package com.institution.management.academic_api.exception.type.employee;

import com.institution.management.academic_api.exception.type.common.ResourceNotFoundException;

public class EmployeeNotFoundException extends ResourceNotFoundException {
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}

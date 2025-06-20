package com.institution.management.academic_api.exception.type.user;

import com.institution.management.academic_api.exception.type.common.InvalidRequestException;

public class InvalidRoleAssignmentException extends InvalidRequestException {
    public InvalidRoleAssignmentException(String message) {
        super(message);
    }
}
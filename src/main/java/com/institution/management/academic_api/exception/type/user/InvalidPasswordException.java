package com.institution.management.academic_api.exception.type.user;

import com.institution.management.academic_api.exception.type.common.InvalidRequestException;

public class InvalidPasswordException extends InvalidRequestException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
package com.institution.management.academic_api.exception.type.user;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class UserAlreadyExistsException extends ConflictException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
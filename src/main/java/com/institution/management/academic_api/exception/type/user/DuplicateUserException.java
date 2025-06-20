package com.institution.management.academic_api.exception.type.user;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class DuplicateUserException extends ConflictException {

    public DuplicateUserException(String message) {
        super(message);
    }
}
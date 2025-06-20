package com.institution.management.academic_api.exception.type.user;

import com.institution.management.academic_api.exception.type.common.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
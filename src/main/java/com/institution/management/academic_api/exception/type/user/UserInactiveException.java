package com.institution.management.academic_api.exception.type.user;

import com.institution.management.academic_api.exception.type.common.InvalidRequestException;

public class UserInactiveException extends InvalidRequestException {
    public UserInactiveException(String message) {
        super(message);
    }
}
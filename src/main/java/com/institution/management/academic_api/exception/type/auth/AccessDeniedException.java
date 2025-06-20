package com.institution.management.academic_api.exception.type.auth;

import com.institution.management.academic_api.exception.type.common.ForbiddenException;

public class AccessDeniedException extends ForbiddenException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
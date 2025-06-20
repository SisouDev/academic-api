package com.institution.management.academic_api.exception.type.common;
import org.springframework.http.HttpStatus;

public abstract class ForbiddenException extends BusinessException {
    protected ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
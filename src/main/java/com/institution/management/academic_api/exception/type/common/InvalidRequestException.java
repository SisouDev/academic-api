package com.institution.management.academic_api.exception.type.common;
import org.springframework.http.HttpStatus;

public abstract class InvalidRequestException extends BusinessException {

    protected InvalidRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
package com.institution.management.academic_api.exception.type.student;

import com.institution.management.academic_api.exception.type.common.InvalidRequestException;

public class InvalidScoreException extends InvalidRequestException {
    public InvalidScoreException(String message) {
        super(message);
    }
}
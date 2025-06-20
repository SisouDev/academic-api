package com.institution.management.academic_api.exception.type.teacher;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class TeacherNotAvailableException extends ConflictException {
    public TeacherNotAvailableException(String message) {
        super(message);
    }
}
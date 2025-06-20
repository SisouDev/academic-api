package com.institution.management.academic_api.exception.type.course;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class SubjectAlreadyExistsInCourseException extends ConflictException {
    public SubjectAlreadyExistsInCourseException(String message) {
        super(message);
    }
}
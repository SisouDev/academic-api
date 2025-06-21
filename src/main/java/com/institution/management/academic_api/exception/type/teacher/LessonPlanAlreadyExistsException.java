package com.institution.management.academic_api.exception.type.teacher;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class LessonPlanAlreadyExistsException extends ConflictException {
    public LessonPlanAlreadyExistsException(String message) {
        super(message);
    }
}

package com.institution.management.academic_api.exception.type.course;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class CourseSectionAlreadyExistsInCourseException extends ConflictException {
    public CourseSectionAlreadyExistsInCourseException(String message) {
        super(message);
    }
}

package com.institution.management.academic_api.exception.type.course;

import com.institution.management.academic_api.exception.type.common.InvalidRequestException;

public class CourseSectionWithoutTeacherException extends InvalidRequestException {
    public CourseSectionWithoutTeacherException(String message) {
        super(message);
    }
}
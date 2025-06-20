package com.institution.management.academic_api.exception.type.course;

import com.institution.management.academic_api.exception.type.common.ResourceNotFoundException;

public class CourseSectionNotFoundException extends ResourceNotFoundException {
    public CourseSectionNotFoundException(String message) {
        super(message);
    }
}
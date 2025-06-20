package com.institution.management.academic_api.exception.type.course;

import com.institution.management.academic_api.exception.type.common.ResourceNotFoundException;

public class CourseNotFoundException extends ResourceNotFoundException {
    public CourseNotFoundException(String message) {
        super(message);
    }
}
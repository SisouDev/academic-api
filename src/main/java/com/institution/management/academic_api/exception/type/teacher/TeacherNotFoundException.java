package com.institution.management.academic_api.exception.type.teacher;

import com.institution.management.academic_api.exception.type.common.ResourceNotFoundException;

public class TeacherNotFoundException extends ResourceNotFoundException{
    public TeacherNotFoundException(String message) {
        super(message);
    }
}
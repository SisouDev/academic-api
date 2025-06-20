package com.institution.management.academic_api.exception.type.teacher;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class TeacherAlreadyAssignedException extends ConflictException {
    public TeacherAlreadyAssignedException(String message) {
        super(message);
    }
}
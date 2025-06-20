package com.institution.management.academic_api.exception.type.student;

import com.institution.management.academic_api.exception.type.common.ConflictException;

public class AttendanceAlreadyRegisteredException extends ConflictException {
    public AttendanceAlreadyRegisteredException(String message) {
        super(message);
    }
}
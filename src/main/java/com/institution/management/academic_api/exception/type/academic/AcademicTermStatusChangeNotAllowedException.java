package com.institution.management.academic_api.exception.type.academic;

import com.institution.management.academic_api.exception.type.common.InvalidRequestException;

public class AcademicTermStatusChangeNotAllowedException extends InvalidRequestException {
    public AcademicTermStatusChangeNotAllowedException(String message) {
        super(message);
    }
}
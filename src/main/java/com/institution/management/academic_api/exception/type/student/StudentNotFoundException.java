package com.institution.management.academic_api.exception.type.student;

import com.institution.management.academic_api.exception.type.common.ResourceNotFoundException;

public class StudentNotFoundException extends ResourceNotFoundException {
  public StudentNotFoundException(String message) {
    super(message);
  }
}
package com.institution.management.academic_api.exception.type.common;
import org.springframework.http.HttpStatus;

public abstract class ResourceNotFoundException extends BusinessException {

  protected ResourceNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
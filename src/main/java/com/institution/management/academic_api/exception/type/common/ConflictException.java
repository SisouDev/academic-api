package com.institution.management.academic_api.exception.type.common;
import org.springframework.http.HttpStatus;

public abstract class ConflictException extends BusinessException {

  protected ConflictException(String message) {
    super(message, HttpStatus.CONFLICT);
  }
}
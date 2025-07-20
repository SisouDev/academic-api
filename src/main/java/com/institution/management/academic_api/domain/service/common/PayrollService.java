package com.institution.management.academic_api.domain.service.common;

import com.institution.management.academic_api.application.dto.common.PayrollRecordDetailsDto;
import com.institution.management.academic_api.domain.model.enums.common.PayrollStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PayrollService {
    Page<PayrollRecordDetailsDto> findAll(PayrollStatus status, Pageable pageable);
    PayrollRecordDetailsDto findById(Long id);
    void markAsPaid(Long id);
}
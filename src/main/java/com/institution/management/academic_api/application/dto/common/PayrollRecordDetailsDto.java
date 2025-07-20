package com.institution.management.academic_api.application.dto.common;

import com.institution.management.academic_api.domain.model.enums.common.PayrollStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PayrollRecordDetailsDto(
        Long id,
        Long personId,
        String personName,
        String personJobPosition,
        LocalDate referenceMonth,
        BigDecimal baseSalary,
        BigDecimal bonuses,
        BigDecimal deductions,
        BigDecimal netPay,
        PayrollStatus status,
        LocalDate paymentDate,
        LocalDateTime createdAt
) {}
package com.institution.management.academic_api.application.dto.common;

import com.institution.management.academic_api.domain.model.enums.common.PayrollStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PayrollRecordSummaryDto(
        Long id,
        String personName,
        String personJobPosition,
        LocalDate referenceMonth,
        BigDecimal netPay,
        PayrollStatus status
) {}
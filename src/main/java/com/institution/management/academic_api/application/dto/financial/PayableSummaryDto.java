package com.institution.management.academic_api.application.dto.financial;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PayableSummaryDto(
        String payableId,
        String type,
        String description,
        BigDecimal amount,
        LocalDate dueDate,
        String status
) {}
package com.institution.management.academic_api.application.dto.employee;

import java.math.BigDecimal;
import java.time.LocalDate;

public record JobHistoryDto(
        Long id,
        String jobPosition,
        String salaryLevel,
        BigDecimal baseSalary,
        LocalDate startDate,
        LocalDate endDate,
        String description
) {}
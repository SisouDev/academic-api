package com.institution.management.academic_api.application.dto.student;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssessmentDefinitionDto(
        Long id,
        String title,
        String type,
        LocalDate assessmentDate,
        BigDecimal weight
) {}
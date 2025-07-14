package com.institution.management.academic_api.application.dto.teacher;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GradebookHeaderDto(
        Long definitionId,
        String title,
        String type,
        LocalDate date,
        BigDecimal weight
) {}
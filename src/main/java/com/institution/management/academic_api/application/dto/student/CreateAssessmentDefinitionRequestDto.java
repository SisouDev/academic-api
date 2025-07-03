package com.institution.management.academic_api.application.dto.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateAssessmentDefinitionRequestDto(
        @NotNull Long courseSectionId,
        @NotBlank String title,
        @NotNull String type,
        LocalDate assessmentDate,
        BigDecimal weight
) {}
package com.institution.management.academic_api.application.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados para registrar uma nova avaliação para uma matrícula.")
public record CreateAssessmentRequestDto(
        @Schema(description = "ID da matrícula à qual esta avaliação pertence.", requiredMode = Schema.RequiredMode.REQUIRED, example = "250")
        Long enrollmentId,

        @NotNull Long assessmentDefinitionId,

        @Schema(description = "Nota obtida na avaliação.", requiredMode = Schema.RequiredMode.REQUIRED, example = "8.5")
        BigDecimal score,

        @Schema(description = "Data em que a avaliação foi realizada.", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-10-15")
        LocalDate assessmentDate,

        @Schema(description = "Tipo da avaliação.", requiredMode = Schema.RequiredMode.REQUIRED, example = "EXAM")
        String type
) {}
package com.institution.management.academic_api.application.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados para atualizar parcialmente uma Avaliação.")
public record UpdateAssessmentRequestDto(
        @Schema(description = "Nova nota obtida na avaliação.", example = "9.0")
        BigDecimal score,

        @Schema(description = "Nova data em que a avaliação foi realizada.", example = "2025-10-16")
        LocalDate assessmentDate,

        @Schema(description = "Novo tipo da avaliação.", example = "PROJECT")
        String type
) {}
package com.institution.management.academic_api.application.dto.student;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Representação de uma avaliação.")
public record AssessmentDto(
        Long id,
        BigDecimal score,
        LocalDate assessmentDate,
        String type,
        String title
) {}
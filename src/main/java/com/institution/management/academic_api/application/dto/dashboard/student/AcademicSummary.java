package com.institution.management.academic_api.application.dto.dashboard.student;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Resumo do desempenho acadêmico.")
public record AcademicSummary(
        BigDecimal overallAverageScore,
        double attendanceRate
) {}
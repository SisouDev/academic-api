package com.institution.management.academic_api.application.dto.dashboard.admin;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para o gráfico de distribuição de alunos por curso.")
public record StudentDistributionData(
        @Schema(description = "Nome do curso.")
        String courseName,
        @Schema(description = "Quantidade de alunos matriculados neste curso.")
        long studentCount
) {}
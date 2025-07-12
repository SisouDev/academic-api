package com.institution.management.academic_api.application.dto.dashboard.teacher;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo da carga de trabalho atual de um professor.")
public record WorkloadSummary(
        @Schema(description = "Número de turmas ativas que o professor está lecionando.")
        int activeClassesCount,

        @Schema(description = "Número total de alunos em todas as turmas ativas.")
        int totalStudentsCount,

        @Schema(description = "Número de disciplinas distintas que o professor leciona.")
        int subjectsTaughtCount
) {}
package com.institution.management.academic_api.application.dto.dashboard.director;

import com.institution.management.academic_api.application.dto.common.ActivityLogDto;
import com.institution.management.academic_api.application.dto.dashboard.admin.GlobalStatsDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO completo com todos os dados estratégicos para o dashboard do Diretor.")
public record DirectorDashboardDto(
        @Schema(description = "Visão geral da instituição (número de alunos, professores, cursos, etc.).")
        GlobalStatsDto generalOverview,

        @Schema(description = "Resumo dos principais indicadores financeiros.")
        FinancialSummaryDto financialSummary,

        @Schema(description = "Indicadores de eficiência acadêmica.")
        AcademicEfficiencyDto academicEfficiency,

        @Schema(description = "Alertas operacionais e gerenciais.")
        OperationalAlertsDto operationalAlerts,

        @Schema(description = "Feed das atividades administrativas mais recentes.")
        List<ActivityLogDto> recentActivities
) {}
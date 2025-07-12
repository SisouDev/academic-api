package com.institution.management.academic_api.application.dto.dashboard.admin;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO completo para o dashboard do Administrador.")
public record AdminDashboardDto(
        GlobalStatsDto globalStats,

        @Schema(description = "Dados para o gráfico de distribuição de alunos.")
        List<StudentDistributionData> studentDistribution,

        @Schema(description = "Lista das 10 atividades mais recentes na plataforma.")
        List<ActivityFeedItem> recentActivityFeed
) {}
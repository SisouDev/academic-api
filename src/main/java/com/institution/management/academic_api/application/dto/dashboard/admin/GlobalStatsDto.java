package com.institution.management.academic_api.application.dto.dashboard.admin;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados estatísticos globais para a dashboard administrativa.")
public record GlobalStatsDto(
        @Schema(description = "Número total de alunos ativos.")
        long activeStudents,

        @Schema(description = "Número total de professores ativos.")
        long activeTeachers,

        @Schema(description = "Número total de empréstimos de livros ativos.")
        long activeLoans,

        @Schema(description = "Número total de chamados de suporte abertos.")
        long openSupportTickets,

        @Schema(description = "Número total de cursos com turmas ativas.")
        long activeCourses,

        @Schema(description = "Número total de usuários no sistema.")
        long totalUsers
) {}
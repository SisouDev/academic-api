package com.institution.management.academic_api.application.dto.dashboard.employee;
import io.swagger.v3.oas.annotations.media.Schema;
import com.institution.management.academic_api.application.dto.announcement.AnnouncementSummaryDto;

import java.util.List;

@Schema(description = "DTO completo para o dashboard de um Técnico de TI.")
public record TechnicianDashboardDto(
        @Schema(description = "Contagem de notificações não lidas.")
        long unreadNotifications,

        @Schema(description = "Contagem de tarefas pessoais pendentes.")
        long pendingTasksCount,

        @Schema(description = "Lista das 5 tarefas mais recentes ou urgentes atribuídas ao técnico.")
        List<TaskSummary> myOpenTasks,

        @Schema(description = "Lista dos últimos 3 anúncios relevantes.")
        List<AnnouncementSummaryDto> recentAnnouncements,

        @Schema(description = "Contagem de chamados de suporte abertos e atribuídos ao técnico.")
        long openSupportTickets,

        @Schema(description = "Número de ativos de TI sob a responsabilidade do técnico.")
        long assignedAssetsCount
) {}

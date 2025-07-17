package com.institution.management.academic_api.application.dto.dashboard.employee;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import com.institution.management.academic_api.application.dto.announcement.AnnouncementSummaryDto;


import java.util.List;

@Schema(description = "DTO completo para o dashboard de um funcionário, com seções específicas para cada cargo.")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmployeeDashboardDto(
        @Schema(description = "Contagem de notificações não lidas.")
        long unreadNotifications,
        @Schema(description = "Contagem de tarefas pessoais pendentes.")
        long pendingTasksCount,
        @Schema(description = "Lista das 5 tarefas mais recentes ou urgentes atribuídas a você.")
        List<TaskSummary> myOpenTasks,
        @Schema(description = "Lista dos últimos 3 anúncios relevantes.")
        List<AnnouncementSummaryDto> recentAnnouncements,
        @Schema(description = "Informações para Bibliotecários. Presente apenas se o funcionário for um bibliotecário.")
        LibrarianSummaryDto librarianInfo,

        @Schema(description = "Informações para Técnicos. Presente apenas se o funcionário for um técnico.")
        TechnicianSummaryDto technicianInfo,

        @Schema(description = "Informações para Analistas de RH. Presente apenas se o funcionário for um analista de RH.")
        HrAnalystSummaryDto hrAnalystInfo
) {}
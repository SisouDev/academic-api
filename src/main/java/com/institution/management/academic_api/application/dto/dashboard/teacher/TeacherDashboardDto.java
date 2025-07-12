package com.institution.management.academic_api.application.dto.dashboard.teacher;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO completo com todos os resumos para o dashboard do professor.")
public record TeacherDashboardDto(
        @Schema(description = "Contagem de notificações não lidas.")
        long unreadNotifications,

        @Schema(description = "Resumo da carga de trabalho atual do professor.")
        WorkloadSummary workload,

        @Schema(description = "Lista das próximas tarefas e eventos importantes.")
        List<UpcomingTaskInfo> upcomingTasks,

        @Schema(description = "Contagem de chamados de suporte ou requisições que requerem a atenção do professor.")
        long pendingRequestsCount
) {}
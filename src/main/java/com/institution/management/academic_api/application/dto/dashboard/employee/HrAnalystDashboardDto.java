package com.institution.management.academic_api.application.dto.dashboard.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO completo com resumos para o dashboard do Analista de RH.")
public record HrAnalystDashboardDto(
        @Schema(description = "Contagem de notificações não lidas.")
        long unreadNotifications,

        @Schema(description = "Contagem de pedidos de licença aguardando revisão.")
        long pendingLeaveRequestsCount,

        @Schema(description = "Contagem de novas contratações no mês atual.")
        long newHiresThisMonth,

        @Schema(description = "Lista dos 5 últimos pedidos de licença que precisam de revisão.")
        List<LeaveRequestSummary> recentLeaveRequests
) {}

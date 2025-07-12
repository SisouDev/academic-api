package com.institution.management.academic_api.application.dto.dashboard.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Resumo de informações relevantes para um Analista de RH.")
public record HrAnalystSummaryDto(
        @Schema(description = "Contagem de pedidos de licença aguardando revisão.")
        long pendingLeaveRequestsCount,

        @Schema(description = "Contagem de novas contratações no mês atual.")
        long newHiresThisMonth,

        @Schema(description = "Lista dos 5 últimos pedidos de licença que precisam de revisão.")
        List<LeaveRequestSummary> recentLeaveRequests
) {}
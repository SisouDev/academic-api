package com.institution.management.academic_api.application.dto.dashboard.director;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Contém alertas e pontos de atenção operacionais.")
public record OperationalAlertsDto(
        @Schema(description = "Número de funcionários com pedidos de afastamento recentes (status PENDING).", example = "3")
        long recentLeaveRequests,

        @Schema(description = "Número de transações de pagamento que falharam ou estão pendentes.", example = "12")
        long failedOrPendingTransactions,

        @Schema(description = "Número de tickets de suporte abertos com prioridade alta.", example = "5")
        long highPrioritySupportTickets,

        @Schema(description = "Número de solicitações internas pendentes de resolução.", example = "8")
        long pendingInternalRequests
) {}
package com.institution.management.academic_api.application.dto.dashboard.employee;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo de informações relevantes para um Técnico de TI.")
public record TechnicianSummaryDto(
        @Schema(description = "Número de chamados de suporte abertos e atribuídos ao técnico.")
        long openSupportTickets,
        @Schema(description = "Número de ativos de TI sob a responsabilidade do técnico.")
        long assignedAssetsCount
) {}
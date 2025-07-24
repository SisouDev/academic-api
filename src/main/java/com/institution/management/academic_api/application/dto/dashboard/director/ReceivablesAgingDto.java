package com.institution.management.academic_api.application.dto.dashboard.director;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Estrutura a inadimplência por tempo de atraso.")
public record ReceivablesAgingDto(
        @Schema(description = "Valores a vencer ou em dia.")
        BigDecimal current,

        @Schema(description = "Valores vencidos entre 1 e 30 dias.")
        BigDecimal overdue30Days,

        @Schema(description = "Valores vencidos entre 31 e 60 dias.")
        BigDecimal overdue60Days,

        @Schema(description = "Valores vencidos entre 61 e 90 dias.")
        BigDecimal overdue90Days,

        @Schema(description = "Valores vencidos há mais de 90 dias.")
        BigDecimal overdueOver90Days
) {}
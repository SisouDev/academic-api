package com.institution.management.academic_api.application.dto.dashboard.director;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Representa o fluxo de caixa para um período específico (ex: um mês).")
public record CashFlowTrendDto(
        @Schema(description = "O período de referência, ex: 'Jan/25', 'Fev/25'.")
        String period,

        @Schema(description = "Total de receitas no período.")
        BigDecimal revenue,

        @Schema(description = "Total de despesas no período.")
        BigDecimal expenses,

        @Schema(description = "Fluxo de caixa líquido (receita - despesa) no período.")
        BigDecimal netFlow
) {}
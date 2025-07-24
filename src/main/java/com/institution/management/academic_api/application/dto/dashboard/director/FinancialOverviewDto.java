package com.institution.management.academic_api.application.dto.dashboard.director;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO com os principais indicadores financeiros para o relatório do diretor.")
public record FinancialOverviewDto(
        @Schema(description = "Receita total acumulada no ano fiscal corrente.")
        BigDecimal totalRevenueYTD,

        @Schema(description = "Despesa total acumulada no ano fiscal corrente.")
        BigDecimal totalExpensesYTD,

        @Schema(description = "Resultado líquido (Receita - Despesa) no ano.")
        BigDecimal netIncomeYTD,

        @Schema(description = "Total de contas a receber (inadimplência).")
        BigDecimal accountsReceivable,

        @Schema(description = "Percentual da margem de lucro no ano.")
        BigDecimal profitMargin
) {}
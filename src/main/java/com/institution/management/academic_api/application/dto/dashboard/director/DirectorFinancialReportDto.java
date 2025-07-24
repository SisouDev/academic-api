package com.institution.management.academic_api.application.dto.dashboard.director;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO consolidado que agrupa todos os relatórios financeiros para a visão do diretor.")
public record DirectorFinancialReportDto(
        @Schema(description = "Visão geral com os principais KPIs financeiros do ano.")
        FinancialOverviewDto overview,

        @Schema(description = "Tendência do fluxo de caixa nos últimos 6 meses.")
        List<CashFlowTrendDto> cashFlowTrend,

        @Schema(description = "Detalhamento das despesas por categoria.")
        List<ExpenseBreakdownDto> expenseBreakdown,

        @Schema(description = "Análise de envelhecimento das contas a receber (inadimplência).")
        ReceivablesAgingDto receivablesAging
) {}
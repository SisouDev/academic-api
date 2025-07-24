package com.institution.management.academic_api.application.dto.dashboard.director;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Contém um resumo dos principais indicadores financeiros para o dashboard do diretor.")
public record FinancialSummaryDto(
        @Schema(description = "Faturamento total do mês corrente.", example = "150000.00")
        BigDecimal monthlyRevenue,

        @Schema(description = "Percentual de inadimplência (transações de mensalidade com status PENDING ou FAILED).", example = "15.5")
        double delinquencyRate,

        @Schema(description = "Número total de bolsas de estudo ativas.", example = "85")
        long activeScholarships,

        @Schema(description = "Valor total concedido em bolsas ativas.", example = "42500.00")
        BigDecimal totalScholarshipValue,

        @Schema(description = "Soma das despesas operacionais aprovadas/pagas no mês.", example = "35000.00")
        BigDecimal operationalExpenses
) {}
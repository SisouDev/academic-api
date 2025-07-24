package com.institution.management.academic_api.application.dto.dashboard.director;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Detalha o total de despesas por uma categoria específica.")
public record ExpenseBreakdownDto(
        @Schema(description = "Nome da categoria da despesa, ex: 'Folha de Pagamento', 'Compras de TI', 'Manutenção'.")
        String category,

        @Schema(description = "Valor total gasto na categoria.")
        BigDecimal totalAmount,

        @Schema(description = "Percentual que esta categoria representa do total de despesas.")
        double percentage
) {}
package com.institution.management.academic_api.application.dto.financial;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Representação resumida de uma transação financeira para extratos.")
public record FinancialTransactionSummaryDto(
        @Schema(description = "ID único da transação.", example = "1")
        Long id,

        @Schema(description = "Descrição da transação.", example = "Mensalidade Ref. 08/2025")
        String description,

        @Schema(description = "Valor da transação.", example = "-750.00")
        BigDecimal amount,

        @Schema(description = "Tipo da transação.", example = "Débito de Mensalidade")
        String type,

        @Schema(description = "Data da transação.", example = "2025-08-10")
        LocalDate transactionDate
) {}
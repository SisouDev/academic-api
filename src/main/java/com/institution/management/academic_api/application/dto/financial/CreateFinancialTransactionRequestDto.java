package com.institution.management.academic_api.application.dto.financial;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados para registrar uma nova transação financeira para um aluno.")
public record CreateFinancialTransactionRequestDto(
        @Schema(description = "ID do aluno ao qual a transação se refere.", requiredMode = Schema.RequiredMode.REQUIRED, example = "101")
        Long studentId,

        @Schema(description = "Descrição da transação.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Mensalidade Referente a 08/2025")
        String description,

        @Schema(description = "Valor da transação. Use valores negativos para débitos (ex: -750.00) e positivos para créditos (ex: 750.00).", requiredMode = Schema.RequiredMode.REQUIRED, example = "-750.00")
        BigDecimal amount,

        @Schema(description = "Tipo da transação.", requiredMode = Schema.RequiredMode.REQUIRED, example = "TUITION_FEE_DEBIT")
        String type,

        @Schema(description = "Data em que a transação ocorreu (ex: data de vencimento ou pagamento).", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-08-10")
        LocalDate transactionDate
) {}
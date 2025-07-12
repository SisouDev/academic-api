package com.institution.management.academic_api.application.dto.financial;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Representação detalhada de uma transação financeira.")
public record FinancialTransactionDetailsDto(
        @Schema(description = "ID da transação.")
        Long id,

        @Schema(description = "Descrição da transação.")
        String description,

        @Schema(description = "Valor da transação.")
        BigDecimal amount,

        @Schema(description = "Tipo da transação.")
        String type,

        @Schema(description = "Data da transação.")
        LocalDate transactionDate,

        @Schema(description = "Data e hora do registro no sistema.")
        LocalDateTime createdAt,

        @Schema(description = "Aluno relacionado à transação.")
        PersonSummaryDto student
) {}
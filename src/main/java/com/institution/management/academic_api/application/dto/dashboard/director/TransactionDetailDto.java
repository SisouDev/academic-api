package com.institution.management.academic_api.application.dto.dashboard.director;

import com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO para exibir detalhes de uma transação financeira em listas e relatórios.")
public record TransactionDetailDto(
        @Schema(description = "ID da transação", example = "101")
        Long transactionId,

        @Schema(description = "Nome da pessoa associada à transação", example = "João da Silva")
        String personName,

        @Schema(description = "Email da pessoa", example = "joao.silva@example.com")
        String personEmail,

        @Schema(description = "Descrição da transação", example = "Mensalidade - Julho/2025")
        String description,

        @Schema(description = "Valor da transação", example = "750.00")
        BigDecimal amount,

        @Schema(description = "Tipo da transação", example = "TUITION")
        TransactionType type,

        @Schema(description = "Status da transação", example = "PENDING")
        TransactionStatus status,

        @Schema(description = "Data de criação da transação")
        LocalDateTime createdAt
) {}
package com.institution.management.academic_api.application.dto.library;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Representação detalhada de um empréstimo.")
public record LoanDetailsDto(
        @Schema(description = "ID único do empréstimo.", example = "1")
        Long id,

        @Schema(description = "Data do empréstimo.")
        LocalDate loanDate,

        @Schema(description = "Data prevista para devolução.")
        LocalDate dueDate,

        @Schema(description = "Data real da devolução (nulo se ainda ativo).")
        LocalDate returnDate,

        @Schema(description = "Status do empréstimo.", example = "Ativo")
        String status,

        @Schema(description = "Item que foi emprestado.")
        LibraryItemSummaryDto item,

        @Schema(description = "Pessoa que pegou o item emprestado.")
        PersonSummaryDto borrower
) {}
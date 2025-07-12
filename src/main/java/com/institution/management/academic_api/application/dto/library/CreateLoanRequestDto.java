package com.institution.management.academic_api.application.dto.library;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Dados para registrar um novo empréstimo de item.")
public record CreateLoanRequestDto(
        @Schema(description = "ID do item da biblioteca a ser emprestado.", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        Long itemId,

        @Schema(description = "ID da pessoa (aluno, professor, etc.) que está pegando o item.", requiredMode = Schema.RequiredMode.REQUIRED, example = "101")
        Long borrowerId,

        @Schema(description = "Data de devolução prevista.", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-10-30")
        LocalDate dueDate
) {}
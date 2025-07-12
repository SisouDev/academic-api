package com.institution.management.academic_api.application.dto.dashboard.employee;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo de informações relevantes para um Bibliotecário.")
public record LibrarianSummaryDto(
        @Schema(description = "Contagem de empréstimos de livros aguardando aprovação.")
        long pendingLoans,
        @Schema(description = "Contagem de livros com devolução atrasada.")
        long overdueBooks
) {}
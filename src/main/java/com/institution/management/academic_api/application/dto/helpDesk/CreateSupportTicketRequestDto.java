package com.institution.management.academic_api.application.dto.helpDesk;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para criar um novo chamado de suporte (ticket).")
public record CreateSupportTicketRequestDto(
        @Schema(description = "Título breve do problema.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Não consigo acessar o Wi-Fi da biblioteca")
        String title,

        @Schema(description = "Descrição completa do problema, incluindo passos para reproduzir, se possível.", requiredMode = Schema.RequiredMode.REQUIRED)
        String description,

        @Schema(description = "Categoria do problema.", requiredMode = Schema.RequiredMode.REQUIRED, example = "NETWORK")
        String category,

        @Schema(description = "Prioridade do chamado.", requiredMode = Schema.RequiredMode.REQUIRED, example = "MEDIUM")
        String priority
) {}
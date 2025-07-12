package com.institution.management.academic_api.application.dto.helpDesk;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representação detalhada de um chamado de suporte.")
public record SupportTicketDetailsDto(
        @Schema(description = "ID do chamado.", example = "1")
        Long id,

        @Schema(description = "Título do chamado.", example = "Não consigo acessar o Wi-Fi da biblioteca")
        String title,

        @Schema(description = "Descrição completa do problema.")
        String description,

        @Schema(description = "Status atual.", example = "Em Atendimento")
        String status,

        @Schema(description = "Prioridade.", example = "Alta")
        String priority,

        @Schema(description = "Categoria.", example = "Rede")
        String category,

        @Schema(description = "Data de abertura.")
        LocalDateTime createdAt,

        @Schema(description = "Data da resolução.")
        LocalDateTime resolvedAt,

        @Schema(description = "Data de fechamento final.")
        LocalDateTime closedAt,

        @Schema(description = "Pessoa que abriu o chamado.")
        PersonSummaryDto requester,

        @Schema(description = "Técnico responsável pelo atendimento.")
        PersonSummaryDto assignee
) {}
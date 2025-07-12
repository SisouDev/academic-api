package com.institution.management.academic_api.application.dto.helpDesk;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Representação resumida de um chamado de suporte.")
public record SupportTicketSummaryDto(
        @Schema(description = "ID único do chamado.", example = "1")
        Long id,

        @Schema(description = "Título do chamado.", example = "Não consigo acessar o Wi-Fi")
        String title,

        @Schema(description = "Status atual.", example = "Aberto")
        String status,

        @Schema(description = "Prioridade.", example = "Média")
        String priority,

        @Schema(description = "Categoria.", example = "Rede")
        String category,

        @Schema(description = "Data de abertura.")
        LocalDateTime createdAt,

        @Schema(description = "Nome de quem abriu o chamado.", example = "Carlos Mendes")
        String requesterName
) {}
package com.institution.management.academic_api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Representação resumida de uma requisição interna.")
public record InternalRequestSummaryDto(
        @Schema(description = "ID da requisição.", example = "101")
        Long id,

        @Schema(description = "Título da requisição.", example = "Projetor da sala 301 não liga")
        String title,

        @Schema(description = "Tipo da requisição.", example = "MAINTENANCE_REQUEST")
        String type,

        @Schema(description = "Status atual.", example = "PENDING")
        String status,

        @Schema(description = "Nível de urgência.", example = "HIGH")
        String urgency,

        @Schema(description = "Data de criação.")
        LocalDateTime createdAt,

        @Schema(description = "Nome da pessoa que abriu o chamado.")
        String requesterName
) {}
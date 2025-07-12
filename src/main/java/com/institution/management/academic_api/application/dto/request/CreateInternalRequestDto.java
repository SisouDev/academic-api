package com.institution.management.academic_api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para criar uma nova requisição interna.")
public record CreateInternalRequestDto(
        @Schema(description = "Título breve da requisição.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Projetor da sala 301 não liga")
        String title,

        @Schema(description = "Descrição completa do problema ou da necessidade.", requiredMode = Schema.RequiredMode.REQUIRED)
        String description,

        @Schema(description = "Tipo da requisição.", requiredMode = Schema.RequiredMode.REQUIRED, example = "MAINTENANCE_REQUEST")
        String type,

        @Schema(description = "Nível de urgência.", requiredMode = Schema.RequiredMode.REQUIRED, example = "HIGH")
        String urgency,

        @Schema(description = "ID do departamento para o qual a requisição se destina.", example = "4")
        Long targetDepartmentId
) {}
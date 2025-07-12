package com.institution.management.academic_api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizar uma requisição interna (geralmente por um admin).")
public record UpdateInternalRequestDto(
        @Schema(description = "Novo status da requisição.", example = "IN_PROGRESS")
        String status,

        @Schema(description = "ID do funcionário que está atendendo a requisição.", example = "15")
        Long handlerId,

        @Schema(description = "Notas ou comentários sobre a resolução do chamado.")
        String resolutionNotes
) {}
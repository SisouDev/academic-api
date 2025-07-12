package com.institution.management.academic_api.application.dto.humanResources;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para um gestor do RH aprovar ou rejeitar uma solicitação de ausência.")
public record ReviewLeaveRequestDto(
        @Schema(description = "Novo status da solicitação.", requiredMode = Schema.RequiredMode.REQUIRED, example = "APPROVED")
        String status,

        @Schema(description = "Comentários ou notas do revisor do RH.", example = "Período de férias aprovado. Boas férias!")
        String reviewerNotes
) {}
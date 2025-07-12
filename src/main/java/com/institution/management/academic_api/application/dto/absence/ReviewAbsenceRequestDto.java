package com.institution.management.academic_api.application.dto.absence;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para um gestor aprovar ou rejeitar uma justificativa de ausência.")
public record ReviewAbsenceRequestDto(
        @Schema(description = "Novo status da justificativa.", requiredMode = Schema.RequiredMode.REQUIRED, example = "APPROVED")
        String status,

        @Schema(description = "Comentários ou notas da pessoa que analisou a justificativa.", example = "Atestado validado.")
        String resolutionNotes
) {}
package com.institution.management.academic_api.application.dto.absence;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Dados para registrar uma nova ausência ou atraso.")
public record CreateAbsenceRequestDto(
        @Schema(description = "ID da pessoa (funcionário/professor) à qual a ausência se refere.", requiredMode = Schema.RequiredMode.REQUIRED, example = "25")
        Long personId,

        @Schema(description = "Tipo da ocorrência.", requiredMode = Schema.RequiredMode.REQUIRED, example = "ABSENCE")
        String type,

        @Schema(description = "Data da falta ou atraso.", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-10-20")
        LocalDate date,

        @Schema(description = "Justificativa detalhada para a ausência.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Consulta médica de rotina.")
        String justification
) {}
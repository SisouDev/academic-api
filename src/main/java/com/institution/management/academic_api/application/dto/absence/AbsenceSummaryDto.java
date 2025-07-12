package com.institution.management.academic_api.application.dto.absence;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Representação resumida de um registro de ausência.")
public record AbsenceSummaryDto(
        @Schema(description = "ID único do registro de ausência.", example = "1")
        Long id,

        @Schema(description = "Nome do funcionário.", example = "Beatriz Carvalho")
        String personName,

        @Schema(description = "Tipo da ocorrência.", example = "Falta")
        String type,

        @Schema(description = "Data da ocorrência.", example = "2025-10-20")
        LocalDate date,

        @Schema(description = "Status da justificativa.", example = "Pendente")
        String status
) {}
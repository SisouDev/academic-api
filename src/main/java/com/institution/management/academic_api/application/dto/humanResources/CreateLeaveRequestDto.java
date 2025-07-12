package com.institution.management.academic_api.application.dto.humanResources;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Dados para solicitar um novo período de ausência (férias, folga, etc.).")
public record CreateLeaveRequestDto(
        @Schema(description = "Tipo da solicitação.", requiredMode = Schema.RequiredMode.REQUIRED, example = "VACATION")
        String type,

        @Schema(description = "Data de início do período de ausência.", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-09-15")
        LocalDate startDate,

        @Schema(description = "Data de término do período de ausência.", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-09-30")
        LocalDate endDate,

        @Schema(description = "Motivo ou comentário adicional para a solicitação.", example = "Férias anuais planejadas.")
        String reason
) {}
package com.institution.management.academic_api.application.dto.humanResources;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Representação resumida de uma solicitação de ausência.")
public record LeaveRequestSummaryDto(
        @Schema(description = "ID único da solicitação.", example = "1")
        Long id,

        @Schema(description = "Nome do funcionário solicitante.", example = "Beatriz Carvalho")
        String requesterName,

        @Schema(description = "Tipo da solicitação.", example = "Férias")
        String type,

        @Schema(description = "Data de início.", example = "2025-09-15")
        LocalDate startDate,

        @Schema(description = "Data de término.", example = "2025-09-30")
        LocalDate endDate,

        @Schema(description = "Status atual da solicitação.", example = "Pendente")
        String status
) {}
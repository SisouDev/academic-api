package com.institution.management.academic_api.application.dto.academic;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Year;

@Schema(description = "Representação resumida de um Período Letivo para listas.")
public record AcademicTermSummaryDto(
        @Schema(description = "ID único do período letivo.", example = "42")
        Long id,

        @Schema(description = "Ano do período letivo.", example = "2025")
        Year year,

        @Schema(description = "Semestre do período letivo.", example = "2")
        Integer semester,

        @Schema(description = "Status atual do período.", example = "IN_PROGRESS")
        String status
) {}
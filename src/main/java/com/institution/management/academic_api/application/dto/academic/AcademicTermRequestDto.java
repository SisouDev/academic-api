package com.institution.management.academic_api.application.dto.academic;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.Year;

@Schema(description = "Dados para criar ou atualizar um Período Letivo.")
public record AcademicTermRequestDto(
        @Schema(description = "ID da instituição à qual este período pertence.", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        Long institutionId,

        @Schema(description = "Ano do período letivo.", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025")
        Year year,

        @Schema(description = "Semestre do período letivo (1 ou 2).", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
        Integer semester,

        @Schema(description = "Data de início do período.", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-08-01")
        LocalDate startDate,

        @Schema(description = "Data de término do período.", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-12-15")
        LocalDate endDate,

        @Schema(description = "Status inicial do período.", requiredMode = Schema.RequiredMode.REQUIRED, example = "PLANNING")
        String status
) {}
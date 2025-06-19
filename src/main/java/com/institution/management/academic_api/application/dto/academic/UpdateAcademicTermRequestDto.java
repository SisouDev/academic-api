package com.institution.management.academic_api.application.dto.academic;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.Year;

@Schema(description = "Dados para atualizar parcialmente um Período Letivo.")
public record UpdateAcademicTermRequestDto(
        @Schema(description = "Novo ano do período letivo.", example = "2026")
        Year year,

        @Schema(description = "Novo semestre do período letivo.", example = "1")
        Integer semester,

        @Schema(description = "Nova data de início do período.", example = "2026-02-15")
        LocalDate startDate,

        @Schema(description = "Nova data de término do período.", example = "2026-07-05")
        LocalDate endDate,

        @Schema(description = "Novo status do período.", example = "COMPLETED")
        String status
) {}
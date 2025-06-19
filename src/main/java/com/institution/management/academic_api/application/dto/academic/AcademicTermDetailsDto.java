package com.institution.management.academic_api.application.dto.academic;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Schema(description = "Representação detalhada de um Período Letivo.")
public record AcademicTermDetailsDto(
        @Schema(description = "ID único do período letivo.", example = "42")
        Long id,

        @Schema(description = "Ano do período letivo.", example = "2025")
        Year year,

        @Schema(description = "Semestre do período letivo.", example = "2")
        Integer semester,

        @Schema(description = "Data de início do período.", example = "2025-08-01")
        LocalDate startDate,

        @Schema(description = "Data de término do período.", example = "2025-12-15")
        LocalDate endDate,

        @Schema(description = "Status atual do período.", example = "IN_PROGRESS")
        String status,

        @Schema(description = "Instituição à qual este período pertence.")
        InstitutionSummaryDto institution,

        @Schema(description = "Lista de turmas (Course Sections) que ocorrem neste período.")
        List<CourseSectionSummaryDto> courseSections
) {}



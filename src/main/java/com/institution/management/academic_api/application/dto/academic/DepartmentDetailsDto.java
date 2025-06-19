package com.institution.management.academic_api.application.dto.academic;
import com.institution.management.academic_api.application.dto.course.CourseSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Representação detalhada de um Departamento.")
public record DepartmentDetailsDto(
        @Schema(description = "ID único do departamento.", example = "15")
        Long id,

        @Schema(description = "Nome completo do departamento.", example = "Departamento de Engenharia de Software")
        String name,

        @Schema(description = "Sigla do departamento.", example = "DESW")
        String acronym,

        @Schema(description = "Lista de cursos oferecidos por este departamento.")
        List<CourseSummaryDto> courses
) {}

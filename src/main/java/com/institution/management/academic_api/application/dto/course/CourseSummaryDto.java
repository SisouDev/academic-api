package com.institution.management.academic_api.application.dto.course;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação resumida de um Curso.")
public record CourseSummaryDto(
        @Schema(description = "ID do curso.", example = "101")
        Long id,

        @Schema(description = "Nome do curso.", example = "Engenharia de Computação")
        String name,

        @Schema(description = "Duração do curso em semestres.", example = "10")
        Integer durationInSemesters,
        
        String departmentName
) {}
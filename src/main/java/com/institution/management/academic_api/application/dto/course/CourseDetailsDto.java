package com.institution.management.academic_api.application.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Representação detalhada de um Curso.")
public record CourseDetailsDto(
        @Schema(description = "ID do curso.", example = "101")
        Long id,

        @Schema(description = "Nome do curso.", example = "Engenharia de Computação")
        String name,

        @Schema(description = "Descrição detalhada do curso.", example = "Curso focado em hardware e software...")
        String description,

        @Schema(description = "Duração do curso em semestres.", example = "10")
        Integer durationInSemesters,

        @Schema(description = "Lista de disciplinas que compõem o curso.")
        List<SubjectSummaryDto> subjectsSummary,

        List<SubjectDetailsDto> subjects
) {}



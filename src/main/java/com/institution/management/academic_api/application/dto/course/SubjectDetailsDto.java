package com.institution.management.academic_api.application.dto.course;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Representação detalhada de uma Disciplina.")
public record SubjectDetailsDto(
        @Schema(description = "ID da disciplina.", example = "55")
        Long id,

        @Schema(description = "Nome da disciplina.", example = "Cálculo Vetorial")
        String name,

        @Schema(description = "Carga horária da disciplina em horas.", example = "90")
        Integer workloadHours,
        Integer semester,
        @Schema(description = "Curso ao qual a disciplina pertence.")
        CourseSummaryDto course,

        @Schema(description = "Lista de turmas (Course Sections) que ofertam esta disciplina.")
        List<CourseSectionSummaryDto> courseSections,

        Long teacherId,

        String teacherName
) {}
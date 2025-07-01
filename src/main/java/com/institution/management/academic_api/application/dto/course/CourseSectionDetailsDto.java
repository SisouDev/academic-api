package com.institution.management.academic_api.application.dto.course;

import com.institution.management.academic_api.application.dto.academic.AcademicTermSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação detalhada de uma Turma.")
public record CourseSectionDetailsDto(
        @Schema(description = "ID da turma.", example = "150")
        Long id,

        @Schema(description = "Nome/código da turma.", example = "Turma A - Noturno")
        String name,

        @Schema(description = "Sala ou local da turma.", example = "Sala 203B")
        String room,

        @Schema(description = "Disciplina ofertada nesta turma.")
        SubjectSummaryDto subject,

        @Schema(description = "Período letivo em que a turma ocorre.")
        AcademicTermSummaryDto academicTerm,

        @Schema(description = "Número de alunos matriculados na turma.", example = "32")
        Integer enrolledStudentsCount
) {}

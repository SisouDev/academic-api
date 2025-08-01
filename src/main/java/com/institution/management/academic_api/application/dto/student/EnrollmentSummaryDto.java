package com.institution.management.academic_api.application.dto.student;

import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação resumida de uma Matrícula.")
public record EnrollmentSummaryDto(
        @Schema(description = "ID da matrícula.", example = "250")
        Long id,

        @Schema(description = "ID da disciplina (Subject).")
        Long subjectId,

        @Schema(description = "Status da matrícula.", example = "ACTIVE")
        String status,

        @Schema(description = "Informações da turma (Course Section) desta matrícula.")
        CourseSectionSummaryDto courseSection,

        @Schema(description = "Total de faltas do aluno nesta matéria.")
        Integer totalAbsences,

        EnrolledCourseSectionInfo courseSectionInfo,
        EnrolledTeacherInfo teacher
) {}
package com.institution.management.academic_api.application.dto.student;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Dados para realizar uma nova matrícula.")
public record CreateEnrollmentRequestDto(
        @Schema(description = "ID do Aluno a ser matriculado.", requiredMode = Schema.RequiredMode.REQUIRED, example = "101")
        Long studentId,

        @Schema(description = "ID da Turma (Course Section) na qual o aluno será matriculado.", requiredMode = Schema.RequiredMode.REQUIRED, example = "150")
        Long courseSectionId,

        @Schema(description = "Data em que a matrícula está sendo feita.", example = "2025-07-25")
        LocalDate enrollmentDate
) {}
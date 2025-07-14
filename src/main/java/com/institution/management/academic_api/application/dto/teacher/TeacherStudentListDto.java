package com.institution.management.academic_api.application.dto.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Representa um aluno em uma turma específica para a lista geral do professor.")
public record TeacherStudentListDto(
        @Schema(description = "ID da Matrícula (Enrollment). Essencial para criar o link de anotações.")
        Long enrollmentId,

        @Schema(description = "Nome completo do aluno.")
        String studentName,

        @Schema(description = "Email do aluno.")
        String studentEmail,

        @Schema(description = "Nome do curso.")
        String courseName,

        @Schema(description = "Nome da disciplina/matéria.")
        String subjectName,

        @Schema(description = "Média do aluno nesta disciplina específica.")
        BigDecimal averageGrade
) {}
package com.institution.management.academic_api.application.dto.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Representa um aluno na lista de uma turma, com sua média específica.")
public record ClassListStudentDto(
        @Schema(description = "ID da Matrícula (Enrollment), para ações específicas.")
        Long enrollmentId,
        @Schema(description = "ID do aluno.")
        Long studentId,
        @Schema(description = "Nome completo do aluno.")
        String studentName,
        @Schema(description = "Email do aluno.")
        String studentEmail,
        @Schema(description = "Média do aluno nesta matéria/turma específica.")
        BigDecimal averageGradeInSection
) {}
package com.institution.management.academic_api.application.dto.teacher;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO com detalhes e resumos de uma turma para a visão do professor.")
public record CourseSectionDetailsForTeacherDto(
        @Schema(description = "ID da turma.")
        Long id,
        @Schema(description = "ID da disciplina (para o link de aulas).")
        Long subjectId,
        @Schema(description = "Nome completo da turma.")
        String sectionName,
        @Schema(description = "Nome da disciplina.")
        String subjectName,

        @Schema(description = "Número total de aulas criadas.")
        int lessonCount,
        @Schema(description = "Número de alunos matriculados.")
        int studentCount,
        @Schema(description = "Número de avaliações pendentes de nota.")
        long pendingAssessmentsCount,
        @Schema(description = "ID do plano de aula, se existir.")
        Long lessonPlanId,

        @Schema(description = "Lista de alunos matriculados nesta turma.")
        List<ClassListStudentDto> enrolledStudents
) {}
package com.institution.management.academic_api.application.dto.teacher;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo de uma turma (CourseSection) para o dashboard do professor.")
public record TeacherCourseSectionDto(
        @Schema(description = "ID da turma (CourseSection).")
        Long id,
        @Schema(description = "ID da disciplina (Subject).")
        Long subjectId,
        @Schema(description = "Nome da turma (ex: 'Turma A - Noturno').")
        String name,
        @Schema(description = "Nome da disciplina.")
        String subjectName,
        @Schema(description = "Nome do curso ao qual a disciplina pertence.")
        String courseName,
        @Schema(description = "Nome do departamento.")
        String departmentName,
        @Schema(description = "Período letivo (ex: '2025/1').")
        String academicTermName,
        @Schema(description = "Sala da turma.")
        String room,
        @Schema(description = "Quantidade de alunos matriculados.")
        int studentCount,
        @Schema(description = "Status atual do período letivo da turma.")
        String status,
        @Schema(description = "Indica se há novas atividades não vistas na turma.")
        boolean hasNewActivity
) {}
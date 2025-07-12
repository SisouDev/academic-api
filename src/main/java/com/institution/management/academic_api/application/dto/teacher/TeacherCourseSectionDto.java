package com.institution.management.academic_api.application.dto.teacher;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo de uma turma (CourseSection) para o dashboard do professor.")
public record TeacherCourseSectionDto(
        @Schema(description = "ID da turma.")
        Long id,

        @Schema(description = "ID da disciplina (Subject).")
        Long subjectId,

        @Schema(description = "Nome da turma (ex: 'Turma A - Noturno').")
        String name,
        @Schema(description = "Nome da disciplina.")
        String subjectName,
        @Schema(description = "Nome do curso ao qual a disciplina pertence.")
        String courseName,
        @Schema(description = "Quantidade de aulas já criadas para esta turma.")
        int totalLessons
) {}
package com.institution.management.academic_api.application.dto.student;

import com.institution.management.academic_api.application.dto.academic.LessonDetailsDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Visão detalhada de uma matéria para o aluno logado, incluindo aulas e conteúdos.")
public record StudentSubjectDetailsDto(
        @Schema(description = "ID da disciplina (Subject).")
        Long subjectId,

        @Schema(description = "ID da turma (CourseSection).")
        Long courseSectionId,

        @Schema(description = "Nome da disciplina.")
        String subjectName,

        @Schema(description = "Nome da turma (CourseSection).")
        String courseSectionName,

        @Schema(description = "Nome do professor da turma.")
        String teacherName,

        @Schema(description = "E-mail do professor.")
        String teacherEmail,

        @Schema(description = "Lista de aulas registradas para esta turma.")
        List<LessonDetailsDto> lessons
) {}
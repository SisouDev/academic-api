package com.institution.management.academic_api.application.dto.teacher;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para criar um novo Plano de Aula.")
public record CreateLessonPlanRequestDto(
        @Schema(description = "ID da Turma (Course Section) à qual este plano pertence.", requiredMode = Schema.RequiredMode.REQUIRED, example = "150")
        Long courseSectionId,

        @Schema(description = "Objetivos da disciplina para a turma.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Capacitar o aluno a resolver integrais duplas e triplas.")
        String objectives,

        @Schema(description = "Conteúdo programático que será abordado.")
        String syllabusContent,

        @Schema(description = "Bibliografia básica e complementar.")
        String bibliography
) {}
package com.institution.management.academic_api.application.dto.teacher;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizar um Plano de Aula. Apenas os campos fornecidos serão alterados.")
public record UpdateLessonPlanRequestDto(
        @Schema(description = "Novos objetivos da disciplina.")
        String objectives,

        @Schema(description = "Novo conteúdo programático.")
        String syllabusContent,

        @Schema(description = "Nova bibliografia.")
        String bibliography
) {}
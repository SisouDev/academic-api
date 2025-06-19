package com.institution.management.academic_api.application.dto.teacher;

import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação de um Plano de Aula.")
public record LessonPlanDto(
        @Schema(description = "ID único do plano de aula.", example = "77")
        Long id,

        @Schema(description = "Objetivos da disciplina para a turma.")
        String objectives,

        @Schema(description = "Conteúdo programático que será abordado.")
        String syllabusContent,

        @Schema(description = "Bibliografia básica e complementar.")
        String bibliography,

        @Schema(description = "Informações resumidas da turma à qual este plano pertence.")
        CourseSectionSummaryDto courseSection
) {}
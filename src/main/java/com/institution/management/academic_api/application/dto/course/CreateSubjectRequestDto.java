package com.institution.management.academic_api.application.dto.course;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para criar uma nova Disciplina.")
public record CreateSubjectRequestDto(
        @Schema(description = "Nome da disciplina.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Cálculo Vetorial")
        String name,

        @Schema(description = "Carga horária da disciplina em horas.", requiredMode = Schema.RequiredMode.REQUIRED, example = "90")
        Integer workloadHours,

        @Schema(description = "ID do Curso ao qual a disciplina pertence.", requiredMode = Schema.RequiredMode.REQUIRED, example = "101")
        Long courseId,

        Integer semester
) {}
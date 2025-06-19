package com.institution.management.academic_api.application.dto.course;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizar parcialmente uma Disciplina.")
public record UpdateSubjectRequestDto(
        @Schema(description = "Novo nome da disciplina.", example = "Algoritmos Avançados")
        String name,

        @Schema(description = "Nova carga horária da disciplina em horas.", example = "120")
        Integer workloadHours
) {}
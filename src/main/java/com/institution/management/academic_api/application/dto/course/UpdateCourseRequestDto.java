package com.institution.management.academic_api.application.dto.course;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizar parcialmente um Curso. Apenas os campos fornecidos serão alterados.")
public record UpdateCourseRequestDto(
        @Schema(description = "Novo nome do curso.", example = "Ciência da Computação")
        String name,

        @Schema(description = "Nova descrição detalhada do curso.")
        String description,

        @Schema(description = "Nova duração do curso em semestres.", example = "8")
        Integer durationInSemesters
) {}
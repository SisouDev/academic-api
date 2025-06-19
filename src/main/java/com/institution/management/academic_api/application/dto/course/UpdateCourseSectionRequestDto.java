package com.institution.management.academic_api.application.dto.course;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizar parcialmente uma Turma.")
public record UpdateCourseSectionRequestDto(
        @Schema(description = "Novo nome/código da turma.", example = "Turma Especial de Férias")
        String name,

        @Schema(description = "Nova sala ou local da turma.", example = "Auditório Principal")
        String room
) {}
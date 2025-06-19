package com.institution.management.academic_api.application.dto.teacher;

import com.institution.management.academic_api.application.dto.common.DocumentDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastrar um novo professor.")
public record CreateTeacherRequestDto(
        @Schema(description = "Primeiro nome.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Beatriz")
        String firstName,

        @Schema(description = "Sobrenome.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Carvalho")
        String lastName,

        @Schema(description = "Email de contato.", requiredMode = Schema.RequiredMode.REQUIRED, example = "beatriz.c@example.com")
        String email,

        @Schema(description = "Formação acadêmica.", requiredMode = Schema.RequiredMode.REQUIRED, example = "PHD")
        String academicBackground,

        @Schema(description = "ID da instituição onde o professor trabalha.", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        Long institutionId,

        DocumentDto document
) {}
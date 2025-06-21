package com.institution.management.academic_api.application.dto.institution;

import com.institution.management.academic_api.application.dto.common.DocumentDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastrar um novo Administrador")
public record CreateInstitutionAdminRequestDto(
        @Schema(description = "ID da instituição à qual este admin pertence.", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        Long institutionId,

        @Schema(description = "Primeiro nome", requiredMode = Schema.RequiredMode.REQUIRED, example = "Joana")
        String firstName,

        @Schema(description = "Sobrenome", requiredMode = Schema.RequiredMode.REQUIRED, example = "Silva")
        String lastName,

        @Schema(description = "E-mail para contato e login", requiredMode = Schema.RequiredMode.REQUIRED, example = "joana.silva@exemplo.com")
        String email,

        @Schema(description = "Telefone para contato", example = "11987654321")
        String phone,

        DocumentDto document
) {}
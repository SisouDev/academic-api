package com.institution.management.academic_api.application.dto.institution;
import com.institution.management.academic_api.domain.model.enums.common.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastrar um novo Administrador")
public record CreateInstitutionAdminRequestDto(
        @Schema(description = "Primeiro nome", requiredMode = Schema.RequiredMode.REQUIRED, example = "Joana")
        String firstName,

        @Schema(description = "Sobrenome", requiredMode = Schema.RequiredMode.REQUIRED, example = "Silva")
        String lastName,

        @Schema(description = "E-mail para contato e login", requiredMode = Schema.RequiredMode.REQUIRED, example = "joana.silva@exemplo.com")
        String email,

        @Schema(description = "Telefone para contato", example = "11987654321")
        String phone,

        @Schema(description = "Tipo do documento (CPF, RG, etc)", requiredMode = Schema.RequiredMode.REQUIRED)
        DocumentType documentType,

        @Schema(description = "Número do documento", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.456.789-00")
        String documentNumber
) {}
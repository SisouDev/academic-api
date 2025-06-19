package com.institution.management.academic_api.application.dto.institution;
import com.institution.management.academic_api.application.dto.common.AddressDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastrar uma nova Instituição.")
public record CreateInstitutionRequestDto(
        @Schema(description = "Nome oficial da instituição.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Universidade Federal de Spring")
        String name,

        @Schema(description = "CNPJ ou outro ID de registro da instituição.", requiredMode = Schema.RequiredMode.REQUIRED, example = "12.345.678/0001-99")
        String registerId,

        @Schema(description = "Endereço completo da sede da instituição.")
        AddressDto address
) {}
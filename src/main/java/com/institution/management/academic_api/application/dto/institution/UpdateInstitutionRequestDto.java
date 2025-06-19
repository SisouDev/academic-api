package com.institution.management.academic_api.application.dto.institution;

import com.institution.management.academic_api.application.dto.common.AddressDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizar parcialmente uma Instituição. Apenas os campos fornecidos serão alterados.")
public record UpdateInstitutionRequestDto(
        @Schema(description = "Novo nome oficial da instituição.", example = "Universidade Federal de Spring Boot")
        String name,

        @Schema(description = "Novo CNPJ ou ID de registro da instituição.", example = "99.888.777/0001-00")
        String registerId,

        @Schema(description = "Novo endereço completo da sede da instituição.")
        AddressDto address
) {}
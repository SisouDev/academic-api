package com.institution.management.academic_api.application.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação resumida de uma Pessoa.")
public record PersonSummaryDto(
        @Schema(description = "ID da pessoa.", example = "101")
        Long id,

        @Schema(description = "Nome completo.", example = "Ana Souza")
        String fullName,

        @Schema(description = "Tipo da pessoa.", example = "STUDENT")
        String personType
) {}
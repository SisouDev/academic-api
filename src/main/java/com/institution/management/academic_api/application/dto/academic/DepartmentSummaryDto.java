package com.institution.management.academic_api.application.dto.academic;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações resumidas de um Departamento.")
public record DepartmentSummaryDto(
        @Schema(description = "ID do departamento.", example = "5")
        Long id,

        @Schema(description = "Nome do departamento.", example = "Departamento de TI")
        String name,

        @Schema(description = "Sigla do departamento.", example = "DTI")
        String acronym
) {}
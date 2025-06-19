package com.institution.management.academic_api.application.dto.institution;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação resumida de uma Instituição.")
public record InstitutionSummaryDto(
        @Schema(description = "ID único da instituição.", example = "1")
        Long id,

        @Schema(description = "Nome oficial da instituição.", example = "Universidade Federal de Spring")
        String name
) {}
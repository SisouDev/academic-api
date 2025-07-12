package com.institution.management.academic_api.application.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação resumida de um material.")
public record MaterialSummaryDto(
        @Schema(description = "ID único do material.", example = "1")
        Long id,

        @Schema(description = "Nome do material.", example = "Resma de Papel A4")
        String name,

        @Schema(description = "Tipo do material.", example = "STATIONERY")
        String type
) {}
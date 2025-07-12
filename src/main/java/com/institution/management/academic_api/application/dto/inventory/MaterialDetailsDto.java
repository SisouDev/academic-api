package com.institution.management.academic_api.application.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação detalhada de um material.")
public record MaterialDetailsDto(
        @Schema(description = "ID único do material.", example = "1")
        Long id,

        @Schema(description = "Nome do material.", example = "Resma de Papel A4")
        String name,

        @Schema(description = "Descrição detalhada do material.", example = "Caixa com 500 folhas, 75g/m².")
        String description,

        @Schema(description = "Tipo do material.", example = "STATIONERY")
        String type
) {}
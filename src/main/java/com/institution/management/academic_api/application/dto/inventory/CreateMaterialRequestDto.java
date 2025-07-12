package com.institution.management.academic_api.application.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastrar um novo material no catálogo.")
public record CreateMaterialRequestDto(
        @Schema(description = "Nome do material.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Resma de Papel A4")
        String name,

        @Schema(description = "Descrição detalhada do material.", example = "Caixa com 500 folhas, 75g/m².")
        String description,

        @Schema(description = "Tipo do material.", requiredMode = Schema.RequiredMode.REQUIRED, example = "STATIONERY")
        String type
) {}
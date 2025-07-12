package com.institution.management.academic_api.application.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizar parcialmente um material. Apenas os campos fornecidos serão alterados.")
public record UpdateMaterialRequestDto(
        @Schema(description = "Novo nome do material.", example = "Resma de Papel A4 Ecológico")
        String name,

        @Schema(description = "Nova descrição do material.")
        String description,

        @Schema(description = "Novo tipo do material.", example = "PRINTING")
        String type
) {}

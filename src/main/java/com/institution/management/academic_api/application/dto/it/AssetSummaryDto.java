package com.institution.management.academic_api.application.dto.it;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação resumida de um ativo de TI.")
public record AssetSummaryDto(
        @Schema(description = "ID único do ativo.", example = "1")
        Long id,

        @Schema(description = "Nome do ativo.", example = "Notebook Dell Vostro 3500")
        String name,

        @Schema(description = "Etiqueta de patrimônio.", example = "IT-00124")
        String assetTag,

        @Schema(description = "Status atual do ativo.", example = "Em Uso")
        String status,

        @Schema(description = "Nome do funcionário com o equipamento.", example = "Carlos Mendes")
        String assignedToName
) {}
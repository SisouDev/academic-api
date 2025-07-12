package com.institution.management.academic_api.application.dto.it;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados para registrar um novo ativo de TI (equipamento).")
public record CreateAssetRequestDto(
        @Schema(description = "Nome ou modelo do equipamento.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Notebook Dell Vostro 3500")
        String name,

        @Schema(description = "Etiqueta de patrimônio do ativo.", example = "IT-00124")
        String assetTag,

        @Schema(description = "Número de série do equipamento.", example = "BRJ12345XYZ")
        String serialNumber,

        @Schema(description = "Data da compra do equipamento.", example = "2025-07-01")
        LocalDate purchaseDate,

        @Schema(description = "Custo de aquisição do equipamento.", example = "4500.50")
        BigDecimal purchaseCost
) {}
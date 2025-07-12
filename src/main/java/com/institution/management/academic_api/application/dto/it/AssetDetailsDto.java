package com.institution.management.academic_api.application.dto.it;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Representação detalhada de um ativo de TI.")
public record AssetDetailsDto(
        @Schema(description = "ID único do ativo.", example = "1")
        Long id,

        @Schema(description = "Nome ou modelo do equipamento.")
        String name,

        @Schema(description = "Etiqueta de patrimônio.")
        String assetTag,

        @Schema(description = "Número de série.")
        String serialNumber,

        @Schema(description = "Data da compra.")
        LocalDate purchaseDate,

        @Schema(description = "Custo de aquisição.")
        BigDecimal purchaseCost,

        @Schema(description = "Status atual do ativo.")
        String status,

        @Schema(description = "Data de registro do ativo no sistema.")
        LocalDateTime createdAt,

        @Schema(description = "Funcionário ao qual o ativo está alocado.")
        PersonSummaryDto assignedTo
) {}
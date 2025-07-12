package com.institution.management.academic_api.application.dto.it;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizar um ativo de TI. Apenas os campos fornecidos serão alterados.")
public record UpdateAssetRequestDto(
        @Schema(description = "Novo status do ativo.", example = "IN_USE")
        String status,

        @Schema(description = "ID do funcionário ao qual o ativo está sendo alocado. Envie nulo para retornar ao estoque.", example = "25")
        Long assignedToId
) {}
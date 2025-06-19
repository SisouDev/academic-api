package com.institution.management.academic_api.application.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações resumidas do usuário para exibição após o login.")
public record UserSummaryDto(
        @Schema(description = "ID do usuário.", example = "50")
        Long id,

        @Schema(description = "Login.", example = "ana.souza")
        String login,

        @Schema(description = "Nome completo da pessoa associada.", example = "Ana Souza")
        String fullName
) {}
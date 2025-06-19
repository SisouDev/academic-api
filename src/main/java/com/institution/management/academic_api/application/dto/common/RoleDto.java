package com.institution.management.academic_api.application.dto.common;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa um perfil de acesso (permissão) no sistema.")
public record RoleDto(
        @Schema(description = "ID único do perfil.", example = "2")
        Long id,

        @Schema(description = "Nome do perfil.", example = "ROLE_MANAGER")
        String name
) {}
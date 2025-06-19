package com.institution.management.academic_api.application.dto.user;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "Contém o conjunto completo de IDs de Roles para substituir os existentes de um usuário.")
public record UpdateUserRolesRequestDto(
        @Schema(description = "A lista completa de IDs de perfis que o usuário deverá ter.", requiredMode = Schema.RequiredMode.REQUIRED)
        Set<Long> roleIds
) {}
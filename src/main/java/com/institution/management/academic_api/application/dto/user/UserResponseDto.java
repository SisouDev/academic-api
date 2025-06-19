package com.institution.management.academic_api.application.dto.user;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.common.RoleDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Representação dos dados de uma conta de usuário.")
public record UserResponseDto(
        @Schema(description = "ID único do usuário.", example = "50")
        Long id,

        @Schema(description = "Login do usuário.", example = "ana.souza")
        String login,

        @Schema(description = "Indica se a conta está ativa.", example = "true")
        boolean isActive,

        @Schema(description = "Informações da pessoa vinculada a esta conta.")
        PersonSummaryDto person,

        @Schema(description = "Perfis de acesso (Roles) do usuário.")
        Set<RoleDto> roles
) {
}
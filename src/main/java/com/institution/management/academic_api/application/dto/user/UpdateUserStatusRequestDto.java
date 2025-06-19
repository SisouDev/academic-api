package com.institution.management.academic_api.application.dto.user;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para alterar o status de ativação de um usuário.")
public record UpdateUserStatusRequestDto(
        @Schema(description = "Define se a conta do usuário está ativa.", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean isActive
) {}

package com.institution.management.academic_api.application.dto.user;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para um usuário alterar sua própria senha.")
public record ChangePasswordRequestDto(
        @Schema(description = "A senha atual do usuário, para verificação.", requiredMode = Schema.RequiredMode.REQUIRED)
        String oldPassword,

        @Schema(description = "A nova senha desejada.", requiredMode = Schema.RequiredMode.REQUIRED)
        String newPassword
) {}
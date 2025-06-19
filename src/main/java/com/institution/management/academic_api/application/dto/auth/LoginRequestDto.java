package com.institution.management.academic_api.application.dto.auth;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para autenticação de um usuário.")
public record LoginRequestDto(
        @Schema(description = "Login do usuário.", requiredMode = Schema.RequiredMode.REQUIRED, example = "ana.souza")
        String login,

        @Schema(description = "Senha do usuário em texto plano.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Senha@Forte123")
        String password
) {}
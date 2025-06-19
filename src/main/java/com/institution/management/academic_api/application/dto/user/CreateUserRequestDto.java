package com.institution.management.academic_api.application.dto.user;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "Dados para criar uma nova conta de usuário.")
public record CreateUserRequestDto(
        @Schema(description = "Login único do usuário.", requiredMode = Schema.RequiredMode.REQUIRED, example = "ana.souza")
        String login,

        @Schema(description = "Senha do usuário (será criptografada no backend).", requiredMode = Schema.RequiredMode.REQUIRED, example = "Senha@Forte123")
        String password,

        @Schema(description = "ID da Pessoa à qual esta conta de usuário será vinculada.", requiredMode = Schema.RequiredMode.REQUIRED, example = "101")
        Long personId,

        @Schema(description = "Conjunto de IDs dos perfis (Roles) a serem atribuídos ao usuário.", example = "[1, 3]")
        Set<Long> roleIds
) {}
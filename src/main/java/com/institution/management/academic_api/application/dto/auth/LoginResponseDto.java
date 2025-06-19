package com.institution.management.academic_api.application.dto.auth;

import com.institution.management.academic_api.application.dto.user.UserSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da API após uma autenticação bem-sucedida.")
public record LoginResponseDto(
        @Schema(description = "Token JWT para ser usado em requisições subsequentes.")
        String token,

        @Schema(description = "Informações resumidas do usuário autenticado.")
        UserSummaryDto user
) {}
package com.institution.management.academic_api.application.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateProfileRequestDto(
        @Schema(description = "Novo e-mail de contato.", example = "novo.email@example.com")
        String email,

        @Schema(description = "Novo número de telefone.", example = "+5511987654321")
        String phone
) {}
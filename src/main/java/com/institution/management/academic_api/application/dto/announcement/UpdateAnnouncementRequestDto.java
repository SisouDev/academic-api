package com.institution.management.academic_api.application.dto.announcement;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Dados para atualizar um aviso existente. Apenas os campos fornecidos serão alterados.")
public record UpdateAnnouncementRequestDto(
        @Schema(description = "Novo título do aviso.")
        String title,

        @Schema(description = "Novo conteúdo do aviso.")
        String content,

        @Schema(description = "Nova data de expiração.")
        LocalDateTime expiresAt
) {}
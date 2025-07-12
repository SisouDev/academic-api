package com.institution.management.academic_api.application.dto.announcement;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Dados para criar um novo aviso.")
public record CreateAnnouncementRequestDto(
        @Schema(description = "Título do aviso.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Manutenção Programada do Sistema")
        String title,

        @Schema(description = "Conteúdo completo do aviso em texto.", requiredMode = Schema.RequiredMode.REQUIRED)
        String content,

        @Schema(description = "Escopo de visibilidade do aviso.", requiredMode = Schema.RequiredMode.REQUIRED, example = "INSTITUTION")
        String scope,

        @Schema(description = "Data e hora em que o aviso deixará de ser exibido. Nulo para não expirar.", example = "2025-12-31T23:59:59")
        LocalDateTime expiresAt,

        @Schema(description = "ID do departamento alvo, se o escopo for 'DEPARTMENT'. Caso contrário, deve ser nulo.", example = "4")
        Long targetDepartmentId
) {}
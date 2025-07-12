package com.institution.management.academic_api.application.dto.announcement;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Representação resumida de um aviso para listas e feeds.")
public record AnnouncementSummaryDto(
        @Schema(description = "ID único do aviso.", example = "1")
        Long id,

        @Schema(description = "Título do aviso.", example = "Manutenção Programada do Sistema")
        String title,

        @Schema(description = "Data de publicação.")
        LocalDateTime createdAt,

        @Schema(description = "Nome do autor do aviso.", example = "Administrador do Sistema")
        String createdByFullName,

        @Schema(description = "Escopo do aviso (ex: 'INSTITUTION', 'Recursos Humanos').")
        String scopeDisplay
) {}
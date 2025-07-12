package com.institution.management.academic_api.application.dto.announcement;

import com.institution.management.academic_api.application.dto.academic.DepartmentSummaryDto;
import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representação detalhada de um aviso.")
public record AnnouncementDetailsDto(
        @Schema(description = "ID único do aviso.", example = "1")
        Long id,

        @Schema(description = "Título do aviso.", example = "Manutenção Programada do Sistema")
        String title,

        @Schema(description = "Conteúdo completo do aviso.")
        String content,

        @Schema(description = "Escopo de visibilidade.", example = "INSTITUTION")
        String scope,

        @Schema(description = "Data e hora de publicação.")
        LocalDateTime createdAt,

        @Schema(description = "Data e hora de expiração (se houver).")
        LocalDateTime expiresAt,

        @Schema(description = "Autor do aviso.")
        PersonSummaryDto createdBy,

        @Schema(description = "Departamento alvo (se aplicável).")
        DepartmentSummaryDto targetDepartment
) {}
package com.institution.management.academic_api.application.dto.dashboard.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Resumo de um anúncio para exibição em listas e dashboards.")
public record AnnouncementSummaryDto(
        @Schema(description = "ID único do anúncio.", example = "123")
        Long id,

        @Schema(description = "Título principal do anúncio.", example = "Feriado de Independência")
        String title,

        @Schema(description = "Escopo do anúncio, para indicar a quem se destina.", example = "GENERAL")
        String scope,

        @Schema(description = "Data e hora em que o anúncio foi criado.")
        LocalDateTime createdAt
) {}
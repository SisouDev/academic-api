package com.institution.management.academic_api.application.dto.calendar;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Dados para atualizar um evento existente. Apenas os campos fornecidos serão alterados.")
public record UpdateCalendarEventRequestDto(
        @Schema(description = "Novo título do evento.")
        String title,

        @Schema(description = "Nova descrição do evento.")
        String description,

        @Schema(description = "Nova data e hora de início.")
        LocalDateTime startTime,

        @Schema(description = "Nova data e hora de término.")
        LocalDateTime endTime,

        @Schema(description = "Novo tipo do evento.")
        String type
) {}
package com.institution.management.academic_api.application.dto.calendar;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Representação resumida de um evento para visualização em calendário.")
public record CalendarEventSummaryDto(
        @Schema(description = "ID único do evento.", example = "1")
        Long id,

        @Schema(description = "Título do evento.", example = "Feriado: Dia do Professor")
        String title,

        @Schema(description = "Data e hora de início.")
        LocalDateTime startTime,

        @Schema(description = "Data e hora de término.")
        LocalDateTime endTime,

        @Schema(description = "Tipo do evento para diferenciação por cores no frontend.", example = "HOLIDAY")
        String type
) {}
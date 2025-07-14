package com.institution.management.academic_api.application.dto.meeting;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representa um item genérico na agenda do usuário (reunião, evento, etc.).")
public record AgendaItemDto(
        @Schema(description = "ID do evento ou reunião.")
        Long id,

        @Schema(description = "Título do item da agenda.")
        String title,
        String description,
        @Schema(description = "Data e hora de início, no formato 'start' para compatibilidade com bibliotecas de calendário.")
        LocalDateTime start,

        @Schema(description = "Data e hora de término, no formato 'end'.")
        LocalDateTime end,

        @Schema(description = "Tipo do item (ex: 'MEETING', 'HOLIDAY') para diferenciação visual.")
        String type,

        @Schema(description = "Flag que indica se este item é uma reunião (permitindo interações como RSVP).")
        boolean isMeeting
) {}
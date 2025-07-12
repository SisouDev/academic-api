package com.institution.management.academic_api.application.dto.meeting;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Representação resumida de uma reunião.")
public record MeetingSummaryDto(
        @Schema(description = "ID único da reunião.", example = "1")
        Long id,

        @Schema(description = "Título da reunião.", example = "Reunião de Alinhamento Semestral")
        String title,

        @Schema(description = "Data e hora de início.", example = "2025-08-01T10:00:00")
        LocalDateTime startTime,

        @Schema(description = "Data e hora de término.", example = "2025-08-01T11:30:00")
        LocalDateTime endTime,

        @Schema(description = "Nome do organizador.", example = "Joana Silva")
        String organizerName
) {}
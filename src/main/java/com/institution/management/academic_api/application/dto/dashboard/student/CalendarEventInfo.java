package com.institution.management.academic_api.application.dto.dashboard.student;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Detalhes de um evento futuro do calendário.")
public record CalendarEventInfo(
        String title,
        String type,
        LocalDateTime startTime
) {}
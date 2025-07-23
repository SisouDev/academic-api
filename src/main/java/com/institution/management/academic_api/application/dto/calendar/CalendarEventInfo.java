package com.institution.management.academic_api.application.dto.calendar;

import java.time.LocalDateTime;

public record CalendarEventInfo(
        String title,
        String type,
        LocalDateTime startTime
) {}
package com.institution.management.academic_api.application.dto.academic;
import java.time.LocalDate;

public record LessonSummaryDto(
        Long id,
        String topic,
        LocalDate lessonDate
) {}
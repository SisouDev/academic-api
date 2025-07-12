package com.institution.management.academic_api.application.dto.academic;
import java.time.LocalDate;

public record UpdateLessonRequestDto(
        String topic,
        String description,
        LocalDate lessonDate
) {}
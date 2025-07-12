package com.institution.management.academic_api.application.dto.academic;
import java.time.LocalDate;
import java.util.List;

public record LessonDetailsDto(
        Long id,
        String topic,
        String description,
        LocalDate lessonDate,
        List<LessonContentDto> contents
) {}
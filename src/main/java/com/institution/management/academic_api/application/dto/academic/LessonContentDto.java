package com.institution.management.academic_api.application.dto.academic;

public record LessonContentDto(
        Long id,
        String type,
        String content,
        int displayOrder
) {}
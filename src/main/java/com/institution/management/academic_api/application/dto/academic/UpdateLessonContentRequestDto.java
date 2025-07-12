package com.institution.management.academic_api.application.dto.academic;

public record UpdateLessonContentRequestDto(
        String type,
        String content,
        int displayOrder
) {}
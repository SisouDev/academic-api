package com.institution.management.academic_api.application.dto.teacher;

public record TeacherNoteDto(
        Long id,
        String content,
        String authorName,
        String createdAt,
        Long enrollmentId
) {}
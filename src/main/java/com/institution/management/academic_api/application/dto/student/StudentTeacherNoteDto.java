package com.institution.management.academic_api.application.dto.student;

public record StudentTeacherNoteDto(
        Long id,
        String content,
        String authorName,
        String subjectName,
        String createdAt
) {}
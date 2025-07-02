package com.institution.management.academic_api.application.dto.student;

public record ClassListStudentDto(
        Long enrollmentId,
        Long studentId,
        String studentName,
        String studentEmail,
        String status
) {}
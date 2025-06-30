package com.institution.management.academic_api.application.dto.course;

public record CourseStudentCountDto(
        String courseName,
        long studentCount
) {}
package com.institution.management.academic_api.application.dto.student;

public record EnrolledCourseSummaryDto(
        String courseSectionName,
        String subjectName,
        String enrollmentStatus,
        double finalGrade,
        int totalAbsencesInCourse
) {}
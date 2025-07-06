package com.institution.management.academic_api.application.dto.student;

public record EnrolledSubjectDto(
        Long courseSectionId,
        String subjectName
) {}
package com.institution.management.academic_api.application.dto.teacher;

import java.util.Map;

public record GradebookStudentRowDto(
        Long enrollmentId,
        String studentName,
        Map<Long, GradebookGradeDto> grades
) {}
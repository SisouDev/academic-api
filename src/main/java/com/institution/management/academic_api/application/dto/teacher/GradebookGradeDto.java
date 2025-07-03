package com.institution.management.academic_api.application.dto.teacher;

import java.math.BigDecimal;

public record GradebookGradeDto(
        Long assessmentId,
        BigDecimal score
) {}
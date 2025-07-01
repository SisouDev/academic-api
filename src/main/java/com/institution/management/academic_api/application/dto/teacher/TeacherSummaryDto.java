package com.institution.management.academic_api.application.dto.teacher;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações resumidas de um Professor.")
public record TeacherSummaryDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String status,
        String academicBackground
) {}
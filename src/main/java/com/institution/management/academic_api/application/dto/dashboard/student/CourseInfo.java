package com.institution.management.academic_api.application.dto.dashboard.student;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações sobre o curso atual do estudante.")
public record CourseInfo(
        String courseName,
        int currentSemester,
        String conclusionForecast
) {}
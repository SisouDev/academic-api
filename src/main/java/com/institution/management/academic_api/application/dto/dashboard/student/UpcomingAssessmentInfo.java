package com.institution.management.academic_api.application.dto.dashboard.student;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Detalhes de uma avaliação futura.")
public record UpcomingAssessmentInfo(
        String title,
        String subject,
        LocalDate date
) {}

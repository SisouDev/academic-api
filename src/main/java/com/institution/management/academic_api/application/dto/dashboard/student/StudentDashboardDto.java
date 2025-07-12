package com.institution.management.academic_api.application.dto.dashboard.student;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Resumo de informações acadêmicas para o dashboard do estudante.")
public record StudentDashboardDto(
        CourseInfo courseInfo,
        AcademicSummary academicSummary,
        UpcomingAssessmentInfo nextAssessment,
        List<CalendarEventInfo> upcomingEvents
) {}
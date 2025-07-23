package com.institution.management.academic_api.application.dto.dashboard.employee;

import com.institution.management.academic_api.application.dto.dashboard.student.CalendarEventInfo;
import com.institution.management.academic_api.application.dto.request.InternalRequestSummaryDto;
import com.institution.management.academic_api.application.dto.announcement.AnnouncementSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO completo com dados para o dashboard de um(a) Secretário(a).")
public record SecretaryDashboardDto(
        long unreadNotifications,
        long pendingTasksCount,
        List<TaskSummary> myOpenTasks,
        List<AnnouncementSummaryDto> recentAnnouncements,

        long pendingRequestsCount,
        List<InternalRequestSummaryDto> recentPendingRequests,
        CalendarEventInfo nextEvent
) {}
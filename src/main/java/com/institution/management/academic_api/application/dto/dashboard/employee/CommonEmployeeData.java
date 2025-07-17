package com.institution.management.academic_api.application.dto.dashboard.employee;
import com.institution.management.academic_api.application.dto.announcement.AnnouncementSummaryDto;

import java.util.List;

public record CommonEmployeeData(
        long unreadNotifications,
        long pendingTasksCount,
        List<TaskSummary> myOpenTasks,
        List<AnnouncementSummaryDto> recentAnnouncements
) {}
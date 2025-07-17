package com.institution.management.academic_api.application.dto.dashboard.employee;
import io.swagger.v3.oas.annotations.media.Schema;
import com.institution.management.academic_api.application.dto.announcement.AnnouncementSummaryDto;


import java.util.List;

@Schema(description = "DTO completo para o dashboard de um Bibliotecário.")
public record LibrarianDashboardDto(
        long unreadNotifications,
        long pendingTasksCount,
        List<TaskSummary> myOpenTasks,
        List<AnnouncementSummaryDto> recentAnnouncements,

        long pendingLoans,
        long overdueBooks,
        long unpaidFines
) {}
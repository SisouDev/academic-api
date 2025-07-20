package com.institution.management.academic_api.application.dto.dashboard.employee;
import com.institution.management.academic_api.application.dto.announcement.AnnouncementSummaryDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "DTO completo para o dashboard do setor financeiro.")
public record FinanceDashboardDto(
        long unreadNotifications,
        long pendingTasksCount,
        List<TaskSummary> myOpenTasks,
        List<AnnouncementSummaryDto> recentAnnouncements,

        BigDecimal totalReceivable,
        BigDecimal totalPayable,
        long pendingPayrollsCount,
        long pendingPurchaseOrdersCount
) {}
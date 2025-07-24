package com.institution.management.academic_api.domain.service.dashboard;

import com.institution.management.academic_api.application.dto.dashboard.admin.GlobalStatsDto;
import com.institution.management.academic_api.application.dto.dashboard.director.DirectorDashboardDto;
import com.institution.management.academic_api.application.dto.dashboard.employee.*;
import com.institution.management.academic_api.domain.model.entities.user.User;
import org.springframework.transaction.annotation.Transactional;

public interface DashboardService {
    GlobalStatsDto getGlobalStats();
    @Transactional(readOnly = true)
    Object getDashboardDataForUser(User user);
    HrAnalystDashboardDto getHrAnalystDashboard(User user);
    TechnicianDashboardDto getTechnicianDashboard(User user);
    LibrarianDashboardDto getLibrarianDashboard(User user);
    FinanceDashboardDto getFinanceDashboard(User user);
    SecretaryDashboardDto getSecretaryDashboard(User user);
    DirectorDashboardDto getDirectorDashboard(User user);
}
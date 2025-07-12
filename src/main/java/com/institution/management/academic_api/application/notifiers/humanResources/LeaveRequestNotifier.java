package com.institution.management.academic_api.application.notifiers.humanResources;

import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.humanResources.LeaveRequest;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import com.institution.management.academic_api.domain.model.enums.humanResources.LeaveRequestStatus;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.service.common.NotificationAudienceService;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeaveRequestNotifier {

    private final NotificationService notificationService;
    private final NotificationAudienceService audienceService;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;



    public void notifyHrOfNewLeaveRequest(LeaveRequest leaveRequest) {
        departmentRepository.findByNameIgnoreCase("Recursos Humanos").ifPresentOrElse(hrDepartment -> {

            List<JobPosition> targetPositions = List.of(
                    JobPosition.HR_ANALYST,
                    JobPosition.COORDINATOR,
                    JobPosition.MANAGER,
                    JobPosition.DIRECTOR
            );

            List<Employee> hrStaffToNotify = employeeRepository.findByDepartmentAndJobPositionIn(hrDepartment, targetPositions);

            if (hrStaffToNotify.isEmpty()) {
                log.warn("Nenhum funcionário do RH encontrado para notificar sobre a solicitação #{}.", leaveRequest.getId());
                return;
            }

            String message = String.format(
                    "Nova solicitação de %s de %s aguardando revisão.",
                    leaveRequest.getType().getDisplayName().toLowerCase(),
                    leaveRequest.getRequester().getFirstName()
            );
            String link = "/admin/hr/leaves/" + leaveRequest.getId();

            hrStaffToNotify.forEach(staff -> {
                if (staff.getUser() != null) {
                    notificationService.createNotification(
                            staff.getUser(),
                            message,
                            link,
                            NotificationType.TASK_ASSIGNED
                    );
                }
            });
            log.info("Notificando {} membros do RH sobre a nova solicitação de ausência #{}.", hrStaffToNotify.size(), leaveRequest.getId());

        }, () -> log.warn("Departamento 'Recursos Humanos' não encontrado. Não foi possível notificar sobre a solicitação #{}.", leaveRequest.getId()));
    }

    public void notifyRequesterOfReview(LeaveRequest leaveRequest) {
        if (leaveRequest.getRequester() == null || leaveRequest.getRequester().getUser() == null) {
            return;
        }

        String decision = leaveRequest.getStatus() == LeaveRequestStatus.APPROVED ? "aprovada" : "rejeitada";
        String message = String.format(
                "Sua solicitação de %s foi %s.",
                leaveRequest.getType().getDisplayName().toLowerCase(),
                decision
        );
        String link = "/my-requests/leaves/" + leaveRequest.getId();

        notificationService.createNotification(
                leaveRequest.getRequester().getUser(),
                message,
                link,
                NotificationType.GENERAL_INFO
        );
    }
}
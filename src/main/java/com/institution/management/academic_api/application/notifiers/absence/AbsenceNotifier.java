package com.institution.management.academic_api.application.notifiers.absence;

import com.institution.management.academic_api.domain.model.entities.absence.Absence;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AbsenceNotifier {

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public void notifyRequesterOfReview(Absence absence) {
        String decision = absence.getStatus().name().equalsIgnoreCase("APPROVED") ? "aprovada" : "rejeitada";
        String message = String.format(
                "Sua solicitação de ausência para a data %s foi %s.",
                absence.getDate(),
                decision
        );
        String link = "/my-absences/" + absence.getId();

        userRepository.findByPerson(absence.getPerson()).ifPresent(user ->
                notificationService.createNotification(
                        user,
                        message,
                        link,
                        NotificationType.GENERAL_INFO
                )
        );
    }

    public void notifyNewAbsenceCreated(Absence absence) {
        departmentRepository.findByNameIgnoreCase("Recursos Humanos").ifPresent(hrDepartment -> {

            List<JobPosition> targetPositions = List.of(
                    JobPosition.COORDINATOR,
                    JobPosition.DIRECTOR,
                    JobPosition.MANAGER,
                    JobPosition.HR_ANALYST
            );

            List<Employee> hrStaffToNotify = employeeRepository.findByDepartmentAndJobPositionIn(hrDepartment, targetPositions);

            String message = String.format(
                    "Nova solicitação de ausência de %s está pendente de revisão.",
                    absence.getPerson().getFirstName()
            );
            String link = "/admin/hr/absences/" + absence.getId();

            hrStaffToNotify.forEach(staff ->
                    notificationService.createNotification(
                            staff.getUser(),
                            message,
                            link,
                            NotificationType.TASK_ASSIGNED
                    )
            );
        });
    }

    public void notifyReviewerOfNewAttachment(Absence absence) {
        if (absence.getReviewedBy() == null) {
            return;
        }

        String message = String.format(
                "Um novo anexo foi adicionado à solicitação de ausência de %s.",
                absence.getPerson().getFirstName()
        );
        String link = "/rh/absences/" + absence.getId();

        userRepository.findByPerson(absence.getReviewedBy()).ifPresent(user ->
                notificationService.createNotification(
                        user,
                        message,
                        link,
                        NotificationType.UPDATE
                )
        );
    }
}
package com.institution.management.academic_api.application.notifiers.academic;

import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DepartmentNotifier {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public void notifyAdminOfNewDepartment(Department department) {
        String message = String.format("Novo departamento criado: %s (%s)",
                department.getName(),
                department.getAcronym());
        String link = "/admin/departments/" + department.getId();
        sendNotificationToAdmins(message, link, NotificationType.GENERAL_INFO);
    }

    public void notifyAdminOfDepartmentUpdate(String oldName, Department updatedDepartment) {
        String message = String.format("O departamento '%s' foi atualizado para '%s'.",
                oldName,
                updatedDepartment.getName());
        String link = "/admin/departments/" + updatedDepartment.getId();
        sendNotificationToAdmins(message, link, NotificationType.UPDATE);
    }

    public void notifyAdminOfDepartmentDeletion(Department department) {
        String message = String.format("O departamento '%s' (%s) foi excluído.",
                department.getName(),
                department.getAcronym());
        String link = "/admin/departments/logs";
        sendNotificationToAdmins(message, link, NotificationType.ALERT);
    }

    private void sendNotificationToAdmins(String message, String link, NotificationType type) {
        List<User> adminsToNotify = userRepository.findByRoles_Name(RoleName.ROLE_ADMIN);
        adminsToNotify.forEach(admin ->
                notificationService.createNotification(admin, message, link, type)
        );
    }
}
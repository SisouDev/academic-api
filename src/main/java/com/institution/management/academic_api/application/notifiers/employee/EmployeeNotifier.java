package com.institution.management.academic_api.application.notifiers.employee;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
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
public class EmployeeNotifier {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public void notifyAdminOfNewEmployee(Employee employee) {
        List<User> adminsToNotify = userRepository.findByRoles_Name(RoleName.ROLE_ADMIN);
        String message = String.format("Novo funcionário contratado: %s %s para o cargo de %s.",
                employee.getFirstName(),
                employee.getLastName(),
                employee.getJobPosition().getDisplayName());
        String link = "/admin/employees/" + employee.getId();
        sendNotificationToAdmins(message, link, NotificationType.GENERAL_INFO);
    }

    public void notifyAdminOfEmployeeDeletion(Employee employee) {
        List<User> adminsToNotify = userRepository.findByRoles_Name(RoleName.ROLE_ADMIN);
        String message = String.format("O funcionário %s %s foi removido do sistema.",
                employee.getFirstName(),
                employee.getLastName());
        String link = "/admin/employees/logs";
        sendNotificationToAdmins(message, link, NotificationType.ALERT);
    }

    private void sendNotificationToAdmins(String message, String link, NotificationType type) {
        List<User> adminsToNotify = userRepository.findByRoles_Name(RoleName.ROLE_ADMIN);
        adminsToNotify.forEach(admin ->
                notificationService.createNotification(admin, message, link, type)
        );
    }
}
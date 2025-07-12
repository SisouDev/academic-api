package com.institution.management.academic_api.application.notifiers.institution;

import com.institution.management.academic_api.domain.model.entities.institution.Institution;
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
public class InstitutionNotifier {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public void notifyAdminOfNewInstitution(Institution institution) {
        String message = String.format("Nova instituição registrada no sistema: %s", institution.getName());
        String link = "/admin/institutions/" + institution.getId();
        sendNotificationToAdmins(message, link, NotificationType.GENERAL_INFO);
    }

    public void notifyAdminOfInstitutionUpdate(Institution institution) {
        String message = String.format("Os dados da instituição '%s' foram atualizados.", institution.getName());
        String link = "/admin/institutions/" + institution.getId();
        sendNotificationToAdmins(message, link, NotificationType.UPDATE);
    }

    public void notifyAdminOfInstitutionDeletion(Institution institution) {
        String message = String.format("A instituição '%s' foi removida do sistema.", institution.getName());
        String link = "/admin/institutions/logs";
        sendNotificationToAdmins(message, link, NotificationType.ALERT);
    }

    private void sendNotificationToAdmins(String message, String link, NotificationType type) {
        List<User> adminsToNotify = userRepository.findByRoles_Name(RoleName.ROLE_ADMIN);
        adminsToNotify.forEach(admin ->
                notificationService.createNotification(admin, message, link, type)
        );
    }
}
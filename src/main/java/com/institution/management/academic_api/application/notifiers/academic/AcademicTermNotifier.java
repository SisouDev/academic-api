package com.institution.management.academic_api.application.notifiers.academic;

import com.institution.management.academic_api.domain.model.entities.academic.AcademicTerm;
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
public class AcademicTermNotifier {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public void notifyAdminOfNewTerm(AcademicTerm term) {
        String message = String.format("Novo período acadêmico criado: %s/%d",
                term.getYear().toString(),
                term.getSemester());
        String link = "/admin/academic-terms/" + term.getId();
        sendNotificationToAdmins(message, link, NotificationType.GENERAL_INFO);
    }

    public void notifyAdminOfTermUpdate(AcademicTerm term) {
        String message = String.format("O período acadêmico %s/%d foi atualizado.",
                term.getYear().toString(),
                term.getSemester());
        String link = "/admin/academic-terms/" + term.getId();
        sendNotificationToAdmins(message, link, NotificationType.UPDATE);
    }

    public void notifyAdminOfTermDeletion(AcademicTerm term) {
        String message = String.format("O período acadêmico %s/%d foi excluído.",
                term.getYear().toString(),
                term.getSemester());
        String link = "/admin/academic-terms/logs";
        sendNotificationToAdmins(message, link, NotificationType.ALERT);
    }

    private void sendNotificationToAdmins(String message, String link, NotificationType type) {
        List<User> adminsToNotify = userRepository.findByRoles_Name(RoleName.ROLE_ADMIN);
        adminsToNotify.forEach(admin ->
                notificationService.createNotification(admin, message, link, type)
        );
    }
}

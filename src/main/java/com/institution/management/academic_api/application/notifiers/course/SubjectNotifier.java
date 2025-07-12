package com.institution.management.academic_api.application.notifiers.course;

import com.institution.management.academic_api.domain.model.entities.course.Subject;
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
public class SubjectNotifier {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public void notifyAdminOfNewSubject(Subject subject) {
        String message = String.format("Nova disciplina criada para o curso de %s: %s",
                subject.getCourse().getName(),
                subject.getName());
        String link = "/admin/subjects/" + subject.getId();
        sendNotificationToAdmins(message, link, NotificationType.GENERAL_INFO);
    }

    public void notifyAdminOfSubjectUpdate(Subject subject) {
        String message = String.format("A disciplina '%s' foi atualizada.", subject.getName());
        String link = "/admin/subjects/" + subject.getId();
        sendNotificationToAdmins(message, link, NotificationType.UPDATE);
    }

    public void notifyAdminOfSubjectDeletion(Subject subject) {
        String message = String.format("A disciplina '%s' do curso de %s foi excluída.",
                subject.getName(),
                subject.getCourse().getName());
        String link = "/admin/subjects/logs";
        sendNotificationToAdmins(message, link, NotificationType.ALERT);
    }

    private void sendNotificationToAdmins(String message, String link, NotificationType type) {
        List<User> adminsToNotify = userRepository.findByRoles_Name(RoleName.ROLE_ADMIN);
        adminsToNotify.forEach(admin ->
                notificationService.createNotification(admin, message, link, type)
        );
    }
}
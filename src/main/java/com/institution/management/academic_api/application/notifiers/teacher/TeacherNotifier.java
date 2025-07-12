package com.institution.management.academic_api.application.notifiers.teacher;

import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
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
public class TeacherNotifier {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public void notifyTeacherOfNewAccount(Teacher teacher) {
        if (teacher.getUser() == null) {
            return;
        }
        String message = String.format("Bem-vindo(a) à equipe, Prof. %s! Sua conta de acesso foi criada.", teacher.getFirstName());
        String link = "/my-profile";
        notificationService.createNotification(teacher.getUser(), message, link, NotificationType.SUCCESS);
    }

    public void notifyAdminOfNewTeacher(Teacher teacher) {
        List<User> adminsToNotify = userRepository.findByRoles_Name(RoleName.ROLE_ADMIN);
        String message = String.format("Novo professor contratado: %s %s.",
                teacher.getFirstName(),
                teacher.getLastName());
        String link = "/admin/teachers/" + teacher.getId();

        adminsToNotify.forEach(admin ->
                notificationService.createNotification(admin, message, link, NotificationType.GENERAL_INFO)
        );
    }

    public void notifyAdminOfTeacherDeletion(Teacher teacher) {
        List<User> adminsToNotify = userRepository.findByRoles_Name(RoleName.ROLE_ADMIN);
        String message = String.format("O professor %s %s foi removido do sistema.",
                teacher.getFirstName(),
                teacher.getLastName());
        String link = "/admin/teachers/logs";
        sendNotificationToAdmins(message, link, NotificationType.ALERT);
    }

    private void sendNotificationToAdmins(String message, String link, NotificationType type) {
        List<User> adminsToNotify = userRepository.findByRoles_Name(RoleName.ROLE_ADMIN);
        adminsToNotify.forEach(admin ->
                notificationService.createNotification(admin, message, link, type)
        );
    }
}
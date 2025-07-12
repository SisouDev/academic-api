package com.institution.management.academic_api.application.notifiers.student;

import com.institution.management.academic_api.domain.model.entities.student.Student;
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
public class StudentNotifier {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public void notifyStudentOfNewAccount(Student student) {
        if (student.getUser() == null) {
            return;
        }
        String message = String.format("Bem-vindo(a), %s! Sua matrícula foi realizada com sucesso.", student.getFirstName());
        String link = "/my-profile";
        notificationService.createNotification(student.getUser(), message, link, NotificationType.SUCCESS);
    }

    public void notifyAdminOfNewStudent(Student student) {
        List<User> adminsToNotify = userRepository.findByRoles_Name(RoleName.ROLE_ADMIN);
        String message = String.format("Novo aluno matriculado: %s %s.", student.getFirstName(), student.getLastName());
        String link = "/admin/students/" + student.getId();

        adminsToNotify.forEach(admin ->
                notificationService.createNotification(admin, message, link, NotificationType.GENERAL_INFO)
        );
    }

    public void notifyStudentOfStatusChange(Student student) {
        if (student.getUser() == null) {
            return;
        }
        String message = String.format("Atenção: O status da sua matrícula foi atualizado para '%s'.", student.getStatus().getDisplayName());
        String link = "/my-profile";
        NotificationType type = (student.getStatus() == com.institution.management.academic_api.domain.model.enums.common.PersonStatus.ACTIVE) ? NotificationType.SUCCESS : NotificationType.ALERT;

        notificationService.createNotification(student.getUser(), message, link, type);
    }
}
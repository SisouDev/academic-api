package com.institution.management.academic_api.application.notifiers.student;

import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollmentNotifier {

    private final NotificationService notificationService;

    public void notifyStudentOfNewEnrollment(Enrollment enrollment) {
        if (enrollment.getStudent() == null || enrollment.getStudent().getUser() == null) {
            return;
        }

        String message = String.format("Você foi matriculado na turma: %s.",
                enrollment.getCourseSection().getName());
        String link = "/my-classes/" + enrollment.getCourseSection().getId();

        notificationService.createNotification(
                enrollment.getStudent().getUser(),
                message,
                link,
                NotificationType.SUCCESS
        );
    }

    public void notifyStudentOfStatusChange(Enrollment enrollment) {
        if (enrollment.getStudent() == null || enrollment.getStudent().getUser() == null) {
            return;
        }

        String message = String.format("O status da sua matrícula na turma '%s' foi atualizado para: %s.",
                enrollment.getCourseSection().getName(),
                enrollment.getStatus().getDisplayName());
        String link = "/my-classes/" + enrollment.getCourseSection().getId();

        notificationService.createNotification(
                enrollment.getStudent().getUser(),
                message,
                link,
                NotificationType.ACADEMIC_UPDATE
        );
    }
}
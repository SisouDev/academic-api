package com.institution.management.academic_api.application.notifiers.student;

import com.institution.management.academic_api.domain.model.entities.student.Assessment;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssessmentNotifier {

    private final NotificationService notificationService;

    public void notifyStudentOfNewGrade(Assessment assessment) {
        if (assessment.getEnrollment() == null || assessment.getEnrollment().getStudent() == null || assessment.getEnrollment().getStudent().getUser() == null) {
            return;
        }

        String message = String.format("Sua nota em '%s' foi lançada: %.2f",
                assessment.getAssessmentDefinition().getTitle(),
                assessment.getScore());
        String link = "/my-grades/class/" + assessment.getEnrollment().getCourseSection().getId();

        notificationService.createNotification(
                assessment.getEnrollment().getStudent().getUser(),
                message,
                link,
                NotificationType.GRADE_POSTED
        );
    }

    public void notifyStudentOfGradeUpdate(Assessment assessment) {
        if (assessment.getEnrollment() == null || assessment.getEnrollment().getStudent() == null || assessment.getEnrollment().getStudent().getUser() == null) {
            return;
        }

        String message = String.format("Sua nota em '%s' foi atualizada para: %.2f",
                assessment.getAssessmentDefinition().getTitle(),
                assessment.getScore());
        String link = "/my-grades/class/" + assessment.getEnrollment().getCourseSection().getId();

        notificationService.createNotification(
                assessment.getEnrollment().getStudent().getUser(),
                message,
                link,
                NotificationType.UPDATE
        );
    }

    public void notifyStudentOfGradeDeletion(Assessment assessment) {
        if (assessment.getEnrollment() == null || assessment.getEnrollment().getStudent() == null || assessment.getEnrollment().getStudent().getUser() == null) {
            return;
        }

        String message = String.format("A nota da avaliação '%s' foi removida pelo professor.",
                assessment.getAssessmentDefinition().getTitle());
        String link = "/my-grades/class/" + assessment.getEnrollment().getCourseSection().getId();

        notificationService.createNotification(
                assessment.getEnrollment().getStudent().getUser(),
                message,
                link,
                NotificationType.ALERT
        );
    }
}
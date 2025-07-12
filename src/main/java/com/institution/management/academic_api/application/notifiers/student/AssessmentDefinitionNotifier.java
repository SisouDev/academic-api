package com.institution.management.academic_api.application.notifiers.student;

import com.institution.management.academic_api.domain.model.entities.student.AssessmentDefinition;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssessmentDefinitionNotifier {

    private final NotificationService notificationService;

    public void notifyStudentsOfNewAssessment(AssessmentDefinition definition) {
        if (definition.getCourseSection() == null || definition.getCourseSection().getEnrollments().isEmpty()) {
            return;
        }

        String message = String.format("Nova avaliação agendada para a turma '%s': %s",
                definition.getCourseSection().getName(),
                definition.getTitle());
        String link = "/my-classes/" + definition.getCourseSection().getId() + "/assessments";

        for (Enrollment enrollment : definition.getCourseSection().getEnrollments()) {
            notificationService.createNotification(
                    enrollment.getStudent().getUser(),
                    message,
                    link,
                    NotificationType.ACADEMIC_UPDATE
            );
        }
    }

    public void notifyStudentsOfAssessmentUpdate(AssessmentDefinition definition) {
        if (definition.getCourseSection() == null || definition.getCourseSection().getEnrollments().isEmpty()) {
            return;
        }

        String message = String.format("A avaliação '%s' da turma '%s' foi atualizada.",
                definition.getTitle(),
                definition.getCourseSection().getName());
        String link = "/my-classes/" + definition.getCourseSection().getId() + "/assessments";

        for (Enrollment enrollment : definition.getCourseSection().getEnrollments()) {
            notificationService.createNotification(
                    enrollment.getStudent().getUser(),
                    message,
                    link,
                    NotificationType.UPDATE
            );
        }
    }

    public void notifyStudentsOfAssessmentDeletion(AssessmentDefinition definition) {
        if (definition.getCourseSection() == null || definition.getCourseSection().getEnrollments().isEmpty()) {
            return;
        }

        String message = String.format("A avaliação '%s' da turma '%s' foi cancelada.",
                definition.getTitle(),
                definition.getCourseSection().getName());
        String link = "/my-classes/" + definition.getCourseSection().getId() + "/assessments";

        for (Enrollment enrollment : definition.getCourseSection().getEnrollments()) {
            notificationService.createNotification(
                    enrollment.getStudent().getUser(),
                    message,
                    link,
                    NotificationType.ALERT
            );
        }
    }
}
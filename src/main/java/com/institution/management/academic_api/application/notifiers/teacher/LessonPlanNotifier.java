package com.institution.management.academic_api.application.notifiers.teacher;

import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.entities.teacher.LessonPlan;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LessonPlanNotifier {

    private final NotificationService notificationService;

    public void notifyStudentsOfNewLessonPlan(LessonPlan lessonPlan) {
        if (lessonPlan.getCourseSection() == null || lessonPlan.getCourseSection().getEnrollments().isEmpty()) {
            return;
        }

        String message = String.format("O plano de aula para a turma '%s' foi publicado.",
                lessonPlan.getCourseSection().getName());
        String link = "/my-classes/" + lessonPlan.getCourseSection().getId() + "/lesson-plan";

        for (Enrollment enrollment : lessonPlan.getCourseSection().getEnrollments()) {
            notificationService.createNotification(
                    enrollment.getStudent().getUser(),
                    message,
                    link,
                    NotificationType.ACADEMIC_UPDATE
            );
        }
    }

    public void notifyStudentsOfLessonPlanUpdate(LessonPlan lessonPlan) {
        if (lessonPlan.getCourseSection() == null || lessonPlan.getCourseSection().getEnrollments().isEmpty()) {
            return;
        }

        String message = String.format("O plano de aula para a turma '%s' foi atualizado.",
                lessonPlan.getCourseSection().getName());
        String link = "/my-classes/" + lessonPlan.getCourseSection().getId() + "/lesson-plan";

        for (Enrollment enrollment : lessonPlan.getCourseSection().getEnrollments()) {
            notificationService.createNotification(
                    enrollment.getStudent().getUser(),
                    message,
                    link,
                    NotificationType.UPDATE
            );
        }
    }

    public void notifyStudentsOfLessonPlanDeletion(LessonPlan lessonPlan) {
        if (lessonPlan.getCourseSection() == null || lessonPlan.getCourseSection().getEnrollments().isEmpty()) {
            return;
        }

        String message = String.format("O plano de aula para a turma '%s' foi removido.",
                lessonPlan.getCourseSection().getName());
        String link = "/my-classes/" + lessonPlan.getCourseSection().getId();

        for (Enrollment enrollment : lessonPlan.getCourseSection().getEnrollments()) {
            notificationService.createNotification(
                    enrollment.getStudent().getUser(),
                    message,
                    link,
                    NotificationType.ALERT
            );
        }
    }
}
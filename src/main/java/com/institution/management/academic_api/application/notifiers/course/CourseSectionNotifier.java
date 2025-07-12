package com.institution.management.academic_api.application.notifiers.course;

import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseSectionNotifier {

    private final NotificationService notificationService;

    public void notifyTeacherOfNewAssignment(CourseSection courseSection) {
        if (courseSection.getTeacher() == null || courseSection.getTeacher().getUser() == null) {
            return;
        }

        String message = String.format("Você foi designado para lecionar a turma: %s", courseSection.getName());
        String link = "/my-classes/" + courseSection.getId();

        notificationService.createNotification(
                courseSection.getTeacher().getUser(),
                message,
                link,
                NotificationType.TASK_ASSIGNED
        );
    }

    public void notifyTeacherOfUpdate(CourseSection courseSection, String oldTeacherName) {
        if (courseSection.getTeacher() == null || courseSection.getTeacher().getUser() == null) {
            return;
        }

        String message = String.format("A turma '%s' foi atualizada. O professor responsável agora é %s.",
                courseSection.getName(),
                courseSection.getTeacher().getFirstName()
        );

        if (!oldTeacherName.equals(courseSection.getTeacher().getFirstName())) {
            String link = "/my-classes/" + courseSection.getId();
            notificationService.createNotification(
                    courseSection.getTeacher().getUser(),
                    message,
                    link,
                    NotificationType.UPDATE
            );
        }
    }


    public void notifyTeacherOfCancellation(CourseSection courseSection) {
        if (courseSection.getTeacher() == null || courseSection.getTeacher().getUser() == null) {
            return;
        }

        String message = String.format("A turma '%s', que você lecionava, foi cancelada.", courseSection.getName());
        String link = "/my-classes/archive";

        notificationService.createNotification(
                courseSection.getTeacher().getUser(),
                message,
                link,
                NotificationType.ALERT
        );
    }
}
package com.institution.management.academic_api.application.notifiers.academic;

import com.institution.management.academic_api.domain.model.entities.academic.Lesson;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LessonNotifier {

    private final NotificationService notificationService;

    public void notifyStudentsOfNewLesson(Lesson lesson) {
        String subjectName = lesson.getCourseSection().getSubject().getName();
        String message = String.format("Nova aula adicionada em '%s': %s", subjectName, lesson.getTopic());
        String link = "/subjects/" + lesson.getCourseSection().getSubject().getId() + "/lessons/" + lesson.getId();

        for (Enrollment enrollment : lesson.getCourseSection().getEnrollments()) {
            notificationService.createNotification(enrollment.getStudent().getUser(), message, link, NotificationType.ACADEMIC_UPDATE);
        }
    }
}
package com.institution.management.academic_api.application.notifiers.teacher;

import com.institution.management.academic_api.domain.model.entities.teacher.TeacherNote;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeacherNoteNotifier {

    private final NotificationService notificationService;

    public void notifyStudentOfNewNote(TeacherNote note) {
        if (note.getEnrollment() == null || note.getEnrollment().getStudent() == null || note.getEnrollment().getStudent().getUser() == null) {
            return;
        }

        String message = String.format("O professor(a) %s adicionou uma nova observação em sua matrícula na turma '%s'.",
                note.getAuthor().getFirstName(),
                note.getEnrollment().getCourseSection().getName());
        String link = "/my-classes/" + note.getEnrollment().getCourseSection().getId() + "/notes";

        notificationService.createNotification(
                note.getEnrollment().getStudent().getUser(),
                message,
                link,
                NotificationType.ACADEMIC_UPDATE
        );
    }

    public void notifyStudentOfNoteDeletion(TeacherNote note) {
        if (note.getEnrollment() == null || note.getEnrollment().getStudent() == null || note.getEnrollment().getStudent().getUser() == null) {
            return;
        }

        String message = String.format("Uma observação do professor(a) %s em '%s' foi removida.",
                note.getAuthor().getFirstName(),
                note.getEnrollment().getCourseSection().getName());
        String link = "/my-classes/" + note.getEnrollment().getCourseSection().getId() + "/notes";

        notificationService.createNotification(
                note.getEnrollment().getStudent().getUser(),
                message,
                link,
                NotificationType.ALERT
        );
    }
}
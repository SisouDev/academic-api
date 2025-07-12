package com.institution.management.academic_api.application.notifiers.course;

import com.institution.management.academic_api.domain.model.entities.course.Course;
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
public class CourseNotifier {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public void notifyAdminOfNewCourse(Course course) {
        String message = String.format("Novo curso criado no departamento de %s: %s",
                course.getDepartment().getName(),
                course.getName());
        String link = "/admin/courses/" + course.getId();
        sendNotificationToAdmins(message, link, NotificationType.GENERAL_INFO);
    }

    public void notifyAdminOfCourseUpdate(Course course) {
        String message = String.format("O curso '%s' foi atualizado.", course.getName());
        String link = "/admin/courses/" + course.getId();
        sendNotificationToAdmins(message, link, NotificationType.UPDATE);
    }

    public void notifyAdminOfCourseDeletion(Course course) {
        String message = String.format("O curso '%s' do departamento de %s foi excluído.",
                course.getName(),
                course.getDepartment().getName());
        String link = "/admin/courses/logs";
        sendNotificationToAdmins(message, link, NotificationType.ALERT);
    }

    private void sendNotificationToAdmins(String message, String link, NotificationType type) {
        List<User> adminsToNotify = userRepository.findByRoles_Name(RoleName.ROLE_ADMIN);
        adminsToNotify.forEach(admin ->
                notificationService.createNotification(admin, message, link, type)
        );
    }
}
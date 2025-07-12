package com.institution.management.academic_api.application.notifiers.announcement;

import com.institution.management.academic_api.domain.model.entities.announcement.Announcement;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.announcement.AnnouncementScope;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.service.common.NotificationAudienceService;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnnouncementNotifier {

    private final NotificationService notificationService;
    private final NotificationAudienceService audienceService;

    public void notifyNewAnnouncement(Announcement announcement) {
        List<User> audience;

        if (announcement.getScope() == AnnouncementScope.DEPARTMENT) {
            audience = audienceService.getUsersForDepartmentScope(announcement.getTargetDepartment().getId());
        } else {
            audience = audienceService.getUsersForInstitutionalScope();
        }

        String message = "Novo aviso publicado: " + announcement.getTitle();
        String link = "/announcements/" + announcement.getId();

        audience.forEach(user -> {
            if (!user.getPerson().getId().equals(announcement.getCreatedBy().getId())) {
                notificationService.createNotification(
                        user,
                        message,
                        link,
                        NotificationType.NEW_ANNOUNCEMENT
                );
            }
        });
    }
}
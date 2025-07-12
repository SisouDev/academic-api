package com.institution.management.academic_api.application.notifiers.calendar;

import com.institution.management.academic_api.domain.model.entities.calendar.CalendarEvent;
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
public class CalendarEventNotifier {

    private final NotificationService notificationService;
    private final NotificationAudienceService audienceService;

    public void notifyNewEvent(CalendarEvent event) {
        String message = "Novo evento no calendário: " + event.getTitle();
        sendNotificationToAudience(event, message, NotificationType.CALENDAR_EVENT);
    }

    public void notifyEventUpdate(CalendarEvent event) {
        String message = "Evento atualizado: " + event.getTitle();
        sendNotificationToAudience(event, message, NotificationType.UPDATE);
    }

    public void notifyEventCancellation(CalendarEvent event) {
        String message = "Evento cancelado: " + event.getTitle();
        sendNotificationToAudience(event, message, NotificationType.ALERT);
    }

    private void sendNotificationToAudience(CalendarEvent event, String message, NotificationType type) {
        List<User> audience = getAudience(event);
        String link = "/calendar/events/" + event.getId();

        audience.forEach(user -> {
            if (event.getCreatedBy() == null || !user.getPerson().getId().equals(event.getCreatedBy().getId())) {
                notificationService.createNotification(user, message, link, type);
            }
        });
    }

    private List<User> getAudience(CalendarEvent event) {
        if (event.getScope() == AnnouncementScope.DEPARTMENT && event.getTargetDepartment() != null) {
            return audienceService.getUsersForDepartmentScope(event.getTargetDepartment().getId());
        }
        return audienceService.getUsersForInstitutionalScope();
    }
}
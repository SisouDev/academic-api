package com.institution.management.academic_api.domain.service.common;

import com.institution.management.academic_api.application.dto.common.NotificationDto;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> findNotificationsForCurrentUser();

    void markAsRead(Long notificationId);

    void createNotification(User recipient, String message, String link, NotificationType type);
}
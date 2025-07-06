package com.institution.management.academic_api.application.service.common;

import com.institution.management.academic_api.application.dto.common.NotificationDto;
import com.institution.management.academic_api.application.mapper.simple.common.NotificationMapper;
import com.institution.management.academic_api.domain.model.entities.common.Notification;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.NotificationStatus;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.repository.common.NotificationRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> findNotificationsForCurrentUser() {
        User currentUser = getCurrentUser();
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        User currentUser = getCurrentUser();
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found."));

        if (!notification.getRecipient().equals(currentUser)) {
            throw new AccessDeniedException("You do not have permission to change this notification.");
        }
        notification.setStatus(NotificationStatus.READ);
    }

    @Override
    @Transactional
    public void createNotification(User recipient, String message, String link, NotificationType type) {
        if (recipient == null) {
            log.warn("Attempt to create notification for null recipient.");
            return;
        }

        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setMessage(message);
        notification.setLink(link);
        notification.setType(type);

        Notification savedNotification = notificationRepository.save(notification);
        log.info("Notification created for user {}: {}", recipient.getUsername(), message);

        NotificationDto dto = notificationMapper.toDto(savedNotification);
        messagingTemplate.convertAndSendToUser(
                recipient.getUsername(),
                "/queue/notifications",
                dto
        );
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new EntityNotFoundException("Logged in user not found."));
    }
}
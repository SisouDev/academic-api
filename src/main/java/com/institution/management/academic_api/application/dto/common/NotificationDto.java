package com.institution.management.academic_api.application.dto.common;

public record NotificationDto(
        Long id,
        String message,
        boolean isRead,
        String link,
        String type,
        String createdAt
) {}
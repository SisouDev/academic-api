package com.institution.management.academic_api.application.dto.common;

public record ActivityLogDto(
        String description,
        String userName,
        String userEmail,
        String timestamp
) {}
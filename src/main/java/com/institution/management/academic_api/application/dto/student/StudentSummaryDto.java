package com.institution.management.academic_api.application.dto.student;

public record StudentSummaryDto(
        Long id,
        String fullName,
        String email,
        String status
) {}
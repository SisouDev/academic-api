package com.institution.management.academic_api.application.dto.common;

public record PersonSearchResultDto(
        Long id,
        String fullName,
        String email,
        String personType
) {}
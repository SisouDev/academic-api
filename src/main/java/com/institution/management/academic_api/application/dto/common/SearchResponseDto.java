package com.institution.management.academic_api.application.dto.common;

import java.util.List;

public record SearchResponseDto(
        List<PersonSearchResultDto> people,
        List<SubjectSearchResultDto> subjects
) {}
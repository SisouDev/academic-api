package com.institution.management.academic_api.application.dto.teacher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTeacherNoteRequestDto(
        @NotNull Long enrollmentId,
        @NotBlank String content
) {}
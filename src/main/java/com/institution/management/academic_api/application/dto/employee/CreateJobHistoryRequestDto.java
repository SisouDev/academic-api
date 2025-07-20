package com.institution.management.academic_api.application.dto.employee;

import jakarta.validation.constraints.NotNull;

public record CreateJobHistoryRequestDto(
        @NotNull Long personId,
        @NotNull Long newSalaryStructureId,
        String description
) {}
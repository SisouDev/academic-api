package com.institution.management.academic_api.application.dto.library;

import com.institution.management.academic_api.domain.model.enums.library.LoanStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateLoanStatusRequestDto(
        @NotNull(message = "O status não pode ser nulo.")
        LoanStatus status
) {}
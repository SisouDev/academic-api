package com.institution.management.academic_api.application.dto.financial;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePurchaseOrderRequestDto(
        @NotBlank String supplier,
        @NotBlank String description,
        @NotNull @Positive BigDecimal amount,
        @NotNull @FutureOrPresent LocalDate dueDate
) {}
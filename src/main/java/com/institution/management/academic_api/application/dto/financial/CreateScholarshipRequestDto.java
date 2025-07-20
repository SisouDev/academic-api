package com.institution.management.academic_api.application.dto.financial;

import com.institution.management.academic_api.domain.model.enums.financial.DiscountType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateScholarshipRequestDto(
        @NotNull Long enrollmentId,
        @NotBlank String name,
        @NotNull DiscountType discountType,
        @NotNull @Positive BigDecimal value,
        @NotNull @FutureOrPresent LocalDate startDate,
        LocalDate endDate
) {}
package com.institution.management.academic_api.application.dto.financial;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePurchaseRequestDto(
        @NotBlank String itemName,
        @NotNull @Min(1) Integer quantity,
        String justification
) {}
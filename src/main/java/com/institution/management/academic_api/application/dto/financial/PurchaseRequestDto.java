package com.institution.management.academic_api.application.dto.financial;

import com.institution.management.academic_api.domain.model.enums.financial.PurchaseRequestStatus;

import java.time.LocalDateTime;

public record PurchaseRequestDto(
        Long id,
        String requesterName,
        String itemName,
        int quantity,
        String justification,
        PurchaseRequestStatus status,
        LocalDateTime createdAt
) {}
package com.institution.management.academic_api.application.dto.financial;

import com.institution.management.academic_api.domain.model.enums.financial.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PurchaseOrderDto(
        Long id,
        String requesterName,
        String supplier,
        String description,
        BigDecimal amount,
        OrderStatus status,
        LocalDate orderDate,
        LocalDate dueDate
) {}
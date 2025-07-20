package com.institution.management.academic_api.domain.model.enums.financial;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum OrderStatus {
    PENDING_APPROVAL("Pending approval"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    PAID("Paid"),
    CANCELLED("Cancelled");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
    public static OrderStatus fromDisplayName(String text) {
        return Stream.of(OrderStatus.values())
                .filter(scope -> scope.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
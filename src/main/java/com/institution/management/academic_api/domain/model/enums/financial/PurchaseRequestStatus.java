package com.institution.management.academic_api.domain.model.enums.financial;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum PurchaseRequestStatus {
    PENDING("Pending"),
    APPROVED_BY_ASSISTANT("Approved by assistant"),
    REJECTED_BY_ASSISTANT("Rejected by assistant"),
    PROCESSED("Processed");

    private final String displayName;

    PurchaseRequestStatus(String displayName) {
        this.displayName = displayName;
    }
    public static PurchaseRequestStatus fromDisplayName(String text) {
        return Stream.of(PurchaseRequestStatus.values())
                .filter(scope -> scope.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

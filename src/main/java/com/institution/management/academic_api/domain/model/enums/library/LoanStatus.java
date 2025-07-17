package com.institution.management.academic_api.domain.model.enums.library;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum LoanStatus {
    ACTIVE("Active"),
    RETURNED("Returned"),
    LATE("Late"),
    LOST("Lost"),
    OVERDUE("Overdue"),
    PENDING("Pending");

    private final String displayName;

    LoanStatus(String displayName) {
        this.displayName = displayName;
    }

    public static LoanStatus fromDisplayName(String text) {
        return Stream.of(LoanStatus.values())
                .filter(e -> e.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

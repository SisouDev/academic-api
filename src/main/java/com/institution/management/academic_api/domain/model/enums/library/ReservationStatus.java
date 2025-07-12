package com.institution.management.academic_api.domain.model.enums.library;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum ReservationStatus {
    PENDING("Pending"),
    ACTIVE("Active"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled"),
    AVAILABLE("Available");

    private final String displayName;

    ReservationStatus(String displayName) {
        this.displayName = displayName;
    }

    public static ReservationStatus fromDisplayName(String text) {
        return Stream.of(ReservationStatus.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

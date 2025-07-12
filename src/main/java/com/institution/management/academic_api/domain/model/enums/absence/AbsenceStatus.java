package com.institution.management.academic_api.domain.model.enums.absence;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum AbsenceStatus {
    PENDING_REVIEW("Pending Review"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String displayName;

    AbsenceStatus(String displayName) {
        this.displayName = displayName;
    }

    public static AbsenceStatus fromDisplayName(String text) {
        return Stream.of(AbsenceStatus.values())
                .filter(type -> type.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
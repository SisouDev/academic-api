package com.institution.management.academic_api.domain.model.enums.student;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum EnrollmentStatus {
    ACTIVE("Active"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    DROPPED("Dropped");

    private final String displayName;

    EnrollmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public static EnrollmentStatus fromDisplayName(String text) {
        return Stream.of(EnrollmentStatus.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

package com.institution.management.academic_api.domain.model.enums.academic;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum AcademicTermStatus {
    PLANNING("Planning"),
    ENROLLMENT_OPEN("Enrollment open"),
    IN_PROGRESS("In progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    AcademicTermStatus(String displayName) {
        this.displayName = displayName;
    }

    public static AcademicTermStatus fromDisplayName(String text) {
        return Stream.of(AcademicTermStatus.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

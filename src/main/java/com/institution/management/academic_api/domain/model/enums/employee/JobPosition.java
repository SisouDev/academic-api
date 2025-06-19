package com.institution.management.academic_api.domain.model.enums.employee;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum JobPosition {
    SECRETARY("Secretary"),
    COORDINATOR("Coordinator"),
    DIRECTOR("Director"),
    TECHNICIAN("Technician"),
    LIBRARIAN("Librarian"),
    ASSISTANT("Assistant");

    private final String displayName;

    JobPosition(String displayName) {
        this.displayName = displayName;
    }

    public static JobPosition fromDisplayName(String text) {
        return Stream.of(JobPosition.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }

}

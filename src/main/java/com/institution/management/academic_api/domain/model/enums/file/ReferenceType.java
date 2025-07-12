package com.institution.management.academic_api.domain.model.enums.file;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum ReferenceType {
    ABSENCE_JUSTIFICATION("Absence Justification"),
    STUDENT_ASSIGNMENT("Student Assignment"),
    PROFILE_PICTURE("Profile Picture"),;

    private final String displayName;

    ReferenceType(String displayName) {
        this.displayName = displayName;
    }

    public static ReferenceType fromDisplayName(String text) {
        return Stream.of(ReferenceType.values())
                .filter(scope -> scope.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
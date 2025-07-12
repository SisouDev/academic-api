package com.institution.management.academic_api.domain.model.enums.absence;
import lombok.Getter;
import java.util.stream.Stream;

@Getter
public enum AbsenceType {
    ABSENCE("Absence"),
    LATENESS("Lateness");

    private final String displayName;

    AbsenceType(String displayName) {
        this.displayName = displayName;
    }

    public static AbsenceType fromDisplayName(String text) {
        return Stream.of(AbsenceType.values())
                .filter(type -> type.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
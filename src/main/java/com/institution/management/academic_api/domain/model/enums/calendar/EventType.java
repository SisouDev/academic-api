package com.institution.management.academic_api.domain.model.enums.calendar;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum EventType {
    MEETING("Meeting"),
    DEADLINE("Deadline"),
    TRAINING("Training"),
    HOLIDAY("Holiday"),
    SCHOOL_EVENT("School Event"),
    CUSTOM("Custom"),
    WORKSHOP("Workshop");

    private final String displayName;

    EventType(String displayName) {
        this.displayName = displayName;
    }

    public static EventType fromDisplayName(String text) {
        return Stream.of(EventType.values())
                .filter(e -> e.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

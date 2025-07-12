package com.institution.management.academic_api.domain.model.enums.meeting;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum MeetingVisibility {
    PUBLIC("Public"),
    PRIVATE("Private");

    private final String displayName;

    MeetingVisibility(String displayName) {
        this.displayName = displayName;
    }

    public static MeetingVisibility fromDisplayName(String text) {
        return Stream.of(MeetingVisibility.values())
                .filter(scope -> scope.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
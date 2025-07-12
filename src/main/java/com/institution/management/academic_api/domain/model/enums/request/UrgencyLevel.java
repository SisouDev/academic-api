package com.institution.management.academic_api.domain.model.enums.request;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum UrgencyLevel {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    CRITICAL("Critical");

    private final String displayName;

    UrgencyLevel(String displayName) {
        this.displayName = displayName;
    }

    public static UrgencyLevel fromDisplayName(String text) {
        return Stream.of(UrgencyLevel.values())
                .filter(e -> e.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
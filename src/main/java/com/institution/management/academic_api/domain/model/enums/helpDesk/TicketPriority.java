package com.institution.management.academic_api.domain.model.enums.helpDesk;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum TicketPriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    CRITICAL("Critical");

    private final String displayName;

    TicketPriority(String displayName) {
        this.displayName = displayName;
    }

    public static TicketPriority fromDisplayName(String text) {
        return Stream.of(TicketPriority.values())
                .filter(e -> e.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

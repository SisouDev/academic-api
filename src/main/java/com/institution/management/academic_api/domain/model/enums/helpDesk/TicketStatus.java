package com.institution.management.academic_api.domain.model.enums.helpDesk;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum TicketStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    WAITING_FOR_USER("Waiting for User"),
    RESOLVED("Resolved"),
    CLOSED("Closed");

    private final String displayName;

    TicketStatus(String displayName) {
        this.displayName = displayName;
    }

    public static TicketStatus fromDisplayName(String text) {
        return Stream.of(TicketStatus.values())
                .filter(e -> e.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}


package com.institution.management.academic_api.domain.model.enums.helpDesk;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum TicketCategory {
    SYSTEM_BUG("System Bug"),
    LOGIN_ISSUE("Login Issue"),
    FEATURE_REQUEST("Feature Request"),
    GENERAL_QUESTION("General Question"),
    ACCOUNT_PROBLEM("Account Problem"),
    OTHER("Other");

    private final String displayName;

    TicketCategory(String displayName) {
        this.displayName = displayName;
    }

    public static TicketCategory fromDisplayName(String text) {
        return Stream.of(TicketCategory.values())
                .filter(e -> e.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
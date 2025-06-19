package com.institution.management.academic_api.domain.model.enums.common;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum PersonStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BLOCKED("Blocked");

    private final String displayName;

    PersonStatus(String displayName) {
        this.displayName = displayName;
    }

    public static PersonStatus fromDisplayName(String text) {
        return Stream.of(PersonStatus.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

package com.institution.management.academic_api.domain.model.enums.financial;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum ScholarshipStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    EXPIRED("Expired");

    private final String displayName;

    ScholarshipStatus(String displayName) {
        this.displayName = displayName;
    }
    public static ScholarshipStatus fromDisplayName(String text) {
        return Stream.of(ScholarshipStatus.values())
                .filter(scope -> scope.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
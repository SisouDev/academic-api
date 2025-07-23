package com.institution.management.academic_api.domain.model.enums.it;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum AssetStatus {
    IN_STOCK("In stock"),
    IN_USE("In use"),
    IN_MAINTENANCE("In Maintenance"),
    DISPOSED("Disposed");

    private final String displayName;

    AssetStatus(String displayName) {
        this.displayName = displayName;
    }

    public static AssetStatus fromDisplayName(String text) {
        return Stream.of(AssetStatus.values())
                .filter(scope -> scope.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

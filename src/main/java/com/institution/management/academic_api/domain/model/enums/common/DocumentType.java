package com.institution.management.academic_api.domain.model.enums.common;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum DocumentType {
    PASSPORT("Passport"),
    NATIONAL_ID("National ID"),
    DRIVERS_LICENSE("Drivers license"),
    RESIDENCE_PERMIT("Residence permit"),
    OTHER("Other");

    private final String displayName;

    DocumentType(String displayName) {
        this.displayName = displayName;
    }

    public static DocumentType fromDisplayName(String text) {
        return Stream.of(DocumentType.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

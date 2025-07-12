package com.institution.management.academic_api.domain.model.enums.library;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum LibraryItemType {
    BOOK("Book"),
    MAGAZINE("Magazine"),
    JOURNAL("Journal"),
    THESIS("Thesis"),
    OTHER("Other");

    private final String displayName;

    LibraryItemType(String displayName) {
        this.displayName = displayName;
    }

    public static LibraryItemType fromDisplayName(String text) {
        return Stream.of(LibraryItemType.values())
                .filter(e -> e.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

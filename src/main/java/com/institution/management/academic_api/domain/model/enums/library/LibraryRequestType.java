package com.institution.management.academic_api.domain.model.enums.library;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum LibraryRequestType {
    BOOK_RESERVATION("Book reservation"),
    BOOK_PURCHASE("Book purchase"),
    STUDY_ROOM_BOOKING("Study room booking"), ;

    private final String displayName;

    LibraryRequestType(String displayName) {
        this.displayName = displayName;
    }

    public static LibraryRequestType fromDisplayName(String text) {
        return Stream.of(LibraryRequestType.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
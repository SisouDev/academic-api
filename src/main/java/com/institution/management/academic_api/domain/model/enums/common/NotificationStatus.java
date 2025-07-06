package com.institution.management.academic_api.domain.model.enums.common;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum NotificationStatus {
    UNREAD("Unread"),
    READ("Read");

    private final String displayName;

    NotificationStatus(String displayName) {
        this.displayName = displayName;
    }

    public static NotificationStatus fromDisplayName(String text) {
        return Stream.of(NotificationStatus.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
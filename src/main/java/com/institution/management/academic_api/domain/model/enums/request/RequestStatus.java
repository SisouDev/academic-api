package com.institution.management.academic_api.domain.model.enums.request;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum RequestStatus {
    PENDING("Pending"),
    IN_PROGRESS( "In progress"),
    COMPLETED( "Completed"),
    REJECTED( "Rejected"),
    CANCELLED("Cancelled");

    private final String displayName;

    RequestStatus(String displayName) {
        this.displayName = displayName;
    }

    public static RequestStatus fromDisplayName(String text) {
        return Stream.of(RequestStatus.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
package com.institution.management.academic_api.domain.model.enums.humanResources;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum LeaveRequestStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String displayName;

    LeaveRequestStatus(String displayName) {
        this.displayName = displayName;
    }

    public static LeaveRequestStatus fromDisplayName(String text) {
        return Stream.of(LeaveRequestStatus.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

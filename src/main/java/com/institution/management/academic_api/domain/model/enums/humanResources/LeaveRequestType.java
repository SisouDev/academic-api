package com.institution.management.academic_api.domain.model.enums.humanResources;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum LeaveRequestType {
    VACATION("Vacation"),
    DAY_OFF("Day off"),
    ADVANCE_PAYMENT("Advance payment"),;

    private final String displayName;

    LeaveRequestType(String displayName) {
        this.displayName = displayName;
    }

    public static LeaveRequestType fromDisplayName(String text) {
        return Stream.of(LeaveRequestType.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
package com.institution.management.academic_api.domain.model.enums.request;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum RequestType {
    MATERIAL_REQUEST("Material request"),
    MAINTENANCE_REQUEST("Maintenance request"),
    HR_REQUEST("HR request");

    private final String displayName;

    RequestType(String displayName) {
        this.displayName = displayName;
    }

    public static RequestType fromDisplayName(String text) {
        return Stream.of(RequestType.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

package com.institution.management.academic_api.domain.model.enums.common;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum RoleName {
    ROLE_ADMIN("Admin"),
    ROLE_USER("User"),
    ROLE_FINANCE("Finance"),
    ROLE_MANAGER("Manager");

    private final String displayName;

    RoleName(String displayName) {
        this.displayName = displayName;
    }

    public static RoleName fromDisplayName(String text) {
        return Stream.of(RoleName.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

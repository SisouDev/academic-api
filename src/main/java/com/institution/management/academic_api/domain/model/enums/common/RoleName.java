package com.institution.management.academic_api.domain.model.enums.common;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum RoleName {
    ROLE_ADMIN("Admin"),
    ROLE_TEACHER("Teacher"),
    ROLE_SECRETARY("Secretary"),
    ROLE_STUDENT("Student"),
    ROLE_USER("User"),
    ROLE_COORDINATOR("Coordinator"),
    ROLE_FINANCE_MANAGER("Finance"),
    ROLE_EMPLOYEE("Employee"),
    ROLE_MANAGER("Manager"),
    ROLE_TECHNICIAN("Technician"),
    ROLE_HR_ANALYST("HR Analyst"),
    ROLE_FINANCE_ASSISTANT("Finance Assistant"),
    ROLE_DIRECTOR("Director"),
    ROLE_LIBRARIAN("Librarian");

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

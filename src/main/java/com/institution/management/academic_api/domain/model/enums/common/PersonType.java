package com.institution.management.academic_api.domain.model.enums.common;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum PersonType {
    STUDENT("Student"),
    TEACHER("Teacher"),
    STAFF("Staff"),
    GUARDIAN("Guardian"),
    EMPLOYEE("Employee");

    private final String displayName;

    PersonType(String displayName) {
        this.displayName = displayName;
    }

    public static PersonType fromDisplayName(String text) {
        return Stream.of(PersonType.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

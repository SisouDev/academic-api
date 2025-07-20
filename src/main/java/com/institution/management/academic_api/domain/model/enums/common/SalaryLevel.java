package com.institution.management.academic_api.domain.model.enums.common;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum SalaryLevel {
    JUNIOR("Junior"),
    MID_LEVEL("Mid level"),
    SENIOR("Senior"),
    LEAD("Lead"),
    PRINCIPAL("Principal");

    private final String displayName;

    SalaryLevel(String displayName) {
        this.displayName = displayName;
    }

    public static SalaryLevel fromDisplayName(String text) {
        return Stream.of(SalaryLevel.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
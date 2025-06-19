package com.institution.management.academic_api.domain.model.enums.teacher;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum AcademicDegree {
    TECHNICAL("Technical"),
    BACHELOR("Bachelor"),
    LICENTIATE("Licentiate"),
    SPECIALIZATION("Specialization"),
    MASTER("Master"),
    PHD("PHD"),
    POSTDOC("Postdoctorate");

    private final String displayName;

    AcademicDegree(String displayName) {
        this.displayName = displayName;
    }

    public static AcademicDegree fromDisplayName(String text) {
        return Stream.of(AcademicDegree.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

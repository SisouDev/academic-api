package com.institution.management.academic_api.domain.model.enums.student;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum AssessmentType {
    EXAM("Exam"),
    QUIZ("Quiz"),
    ASSIGNMENT("Assignment"),
    PROJECT("Project"),
    PARTICIPATION("Participation");

    private final String displayName;

    AssessmentType(String displayName) {
        this.displayName = displayName;
    }

    public static AssessmentType fromDisplayName(String text) {
        return Stream.of(AssessmentType.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

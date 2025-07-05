package com.institution.management.academic_api.domain.model.enums.student;

import lombok.Getter;

import java.util.stream.Stream;
@Getter
public enum DailyAttendanceStatus {
    PRESENT("Present"),
    ABSENT("Absent"),
    UNDEFINED("Undefined");

    private final String displayName;

    DailyAttendanceStatus(String displayName) {
        this.displayName = displayName;
    }

    public static DailyAttendanceStatus fromDisplayName(String text) {
        return Stream.of(DailyAttendanceStatus.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

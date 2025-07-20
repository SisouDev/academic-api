package com.institution.management.academic_api.domain.model.enums.common;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum NotificationType {
    GRADE_POSTED("Grade posted"),
    NEW_ANNOUNCEMENT("New announcement"),
    ATTENDANCE_UPDATE("Attendance update"),
    TEACHER_NOTE("Teacher note"),
    SYSTEM_ALERT("System alert"),
    GENERAL_INFO("General info"),
    CREATE("Create"),
    UPDATE("Update"),
    CALENDAR_EVENT("Calendar event"),
    TASK_ASSIGNED("Task assigned"),
    FINANCIAL("Financial"),
    ALERT("Alert"),
    SUCCESS("Success"),
    WARNING("Warning"),
    ACTION_REQUIRED("Action required"),
    INVITATION("Invitation"),
    SECURITY_ALERT("Security alert"),
    ACADEMIC_UPDATE("Academic update"),
    LIBRARY_UPDATE("Library update");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public static NotificationType fromDisplayName(String text) {
        return Stream.of(NotificationType.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

package com.institution.management.academic_api.domain.model.enums.tasks;
import lombok.Getter;
import java.util.stream.Stream;

@Getter
public enum TaskStatus {
    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    DONE("Done"),
    CANCELLED("Cancelled");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public static TaskStatus fromDisplayName(String text) {
        return Stream.of(TaskStatus.values())
                .filter(status -> status.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
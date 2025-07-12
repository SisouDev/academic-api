package com.institution.management.academic_api.domain.model.enums.announcement;
import lombok.Getter;
import java.util.stream.Stream;

@Getter
public enum AnnouncementScope {
    INSTITUTION("Institution-wide"),
    DEPARTMENT("Department-specific");

    private final String displayName;

    AnnouncementScope(String displayName) {
        this.displayName = displayName;
    }

    public static AnnouncementScope fromDisplayName(String text) {
        return Stream.of(AnnouncementScope.values())
                .filter(scope -> scope.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
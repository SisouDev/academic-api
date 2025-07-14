package com.institution.management.academic_api.domain.model.enums.meeting;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum MeetingParticipantStatus {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    DECLINED("Declined"),
    TENTATIVE("Tentative");

    private final String displayName;

    MeetingParticipantStatus(String displayName) {
        this.displayName = displayName;
    }

    public static MeetingParticipantStatus fromDisplayName(String text) {
        return Stream.of(MeetingParticipantStatus.values())
                .filter(scope -> scope.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
package com.institution.management.academic_api.application.notifiers.meeting;

import com.institution.management.academic_api.domain.model.entities.meeting.Meeting;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingNotifier {

    private final NotificationService notificationService;

    public void notifyParticipantsOfNewMeeting(Meeting meeting) {
        String message = String.format("Você foi convidado para a reunião: '%s'", meeting.getTitle());
        String link = "/calendar/meetings/" + meeting.getId();

        meeting.getParticipants().forEach(participant -> {
            if (!participant.getId().equals(meeting.getOrganizer().getId())) {
                notificationService.createNotification(
                        participant.getUser(),
                        message,
                        link,
                        NotificationType.INVITATION
                );
            }
        });
    }

    public void notifyParticipantsOfCancellation(Meeting meeting) {
        String message = String.format("A reunião '%s' agendada para %s foi cancelada.",
                meeting.getTitle(),
                meeting.getStartTime().toLocalDate()
        );
        String link = "/calendar";

        meeting.getParticipants().forEach(participant -> {
            if (!participant.getId().equals(meeting.getOrganizer().getId())) {
                notificationService.createNotification(
                        participant.getUser(),
                        message,
                        link,
                        NotificationType.ALERT
                );
            }
        });
    }
}
package com.institution.management.academic_api.domain.factory.meeting;

import com.institution.management.academic_api.application.dto.meeting.CreateMeetingRequestDto;
import com.institution.management.academic_api.application.mapper.simple.meeting.MeetingMapper;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.meeting.Meeting;
import com.institution.management.academic_api.domain.model.entities.meeting.MeetingParticipant;
import com.institution.management.academic_api.domain.model.enums.meeting.MeetingVisibility;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MeetingFactory {

    private final MeetingMapper meetingMapper;
    private final PersonRepository personRepository;

    public Meeting create(CreateMeetingRequestDto dto, Person organizer) {
        Meeting meeting = meetingMapper.toEntity(dto);

        meeting.setOrganizer(organizer);
        meeting.setCreatedAt(LocalDateTime.now());
        MeetingVisibility visibility = MeetingVisibility.fromDisplayName(dto.visibility());
        meeting.setVisibility(visibility);

        Set<Long> allParticipantIds = new HashSet<>();
        if (dto.participantIds() != null) {
            allParticipantIds.addAll(dto.participantIds());
        }
        allParticipantIds.add(organizer.getId());

        List<Person> allParticipantsList = personRepository.findAllById(allParticipantIds);

        Set<MeetingParticipant> participants = allParticipantsList.stream()
                .map(person -> {
                    MeetingParticipant participant = new MeetingParticipant();
                    participant.setMeeting(meeting);
                    participant.setParticipant(person);
                    return participant;
                })
                .collect(Collectors.toSet());

        meeting.setParticipants(participants);
        return meeting;
    }
}
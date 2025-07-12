package com.institution.management.academic_api.domain.factory.meeting;

import com.institution.management.academic_api.application.dto.meeting.CreateMeetingRequestDto;
import com.institution.management.academic_api.application.mapper.simple.meeting.MeetingMapper;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.meeting.Meeting;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MeetingFactory {

    private final MeetingMapper meetingMapper;
    private final PersonRepository personRepository;

    public Meeting create(CreateMeetingRequestDto dto, Person organizer) {
        Meeting meeting = meetingMapper.toEntity(dto);

        meeting.setOrganizer(organizer);
        meeting.setCreatedAt(LocalDateTime.now());

        if (dto.participantIds() != null && !dto.participantIds().isEmpty()) {
            Set<Person> participants = new HashSet<>(personRepository.findAllById(dto.participantIds()));
            meeting.setParticipants(participants);
        }

        meeting.getParticipants().add(organizer);

        return meeting;
    }
}
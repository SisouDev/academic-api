package com.institution.management.academic_api.application.service.meeting;

import com.institution.management.academic_api.application.dto.meeting.CreateMeetingRequestDto;
import com.institution.management.academic_api.application.dto.meeting.MeetingDetailsDto;
import com.institution.management.academic_api.application.dto.meeting.UpdateMeetingRequestDto;
import com.institution.management.academic_api.application.mapper.simple.meeting.MeetingMapper;
import com.institution.management.academic_api.application.notifiers.meeting.MeetingNotifier;
import com.institution.management.academic_api.domain.factory.meeting.MeetingFactory;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.meeting.Meeting;
import com.institution.management.academic_api.domain.model.entities.meeting.MeetingParticipant;
import com.institution.management.academic_api.domain.model.enums.meeting.MeetingParticipantStatus;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.meeting.MeetingParticipantRepository;
import com.institution.management.academic_api.domain.repository.meeting.MeetingRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import com.institution.management.academic_api.domain.service.meeting.MeetingService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final PersonRepository personRepository;
    private final MeetingFactory meetingFactory;
    private final MeetingMapper meetingMapper;
    private final NotificationService notificationService;
    private final MeetingNotifier meetingNotifier;
    private final MeetingParticipantRepository meetingParticipantRepository;

    @Override
    @Transactional
    @LogActivity("Agendou uma nova reunião.")
    public MeetingDetailsDto create(CreateMeetingRequestDto dto, String organizerEmail) {
        Person organizer = personRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new EntityNotFoundException("Organizer not found with email: " + organizerEmail));

        Meeting newMeeting = meetingFactory.create(dto, organizer);
        Meeting savedMeeting = meetingRepository.save(newMeeting);

        meetingNotifier.notifyParticipantsOfNewMeeting(savedMeeting);

        return meetingMapper.toDetailsDto(savedMeeting);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou uma reunião.")
    public MeetingDetailsDto update(Long id, UpdateMeetingRequestDto dto) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found with id: " + id));

        meetingMapper.updateFromDto(dto, meeting);
        return meetingMapper.toDetailsDto(meeting);
    }

    @Override
    @Transactional
    @LogActivity("Cancelou uma reunião.")
    public void delete(Long id) {
        Meeting meetingToDelete = meetingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found with id: " + id));

        meetingNotifier.notifyParticipantsOfCancellation(meetingToDelete);

        meetingRepository.delete(meetingToDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public MeetingDetailsDto findById(Long id) {
        return meetingRepository.findById(id)
                .map(meetingMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found with id: " + id));
    }

    @Override
    @Transactional
    @LogActivity("Respondeu a um convite de reunião (RSVP).")
    public MeetingDetailsDto rsvp(Long meetingId, String userEmail, MeetingParticipantStatus status) {
        Person userPerson = personRepository.findByUser_Login(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + userEmail));

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new EntityNotFoundException("Reunião não encontrada: " + meetingId));

        MeetingParticipant participant = meetingParticipantRepository.findByMeetingAndParticipant(meeting, userPerson)
                .orElseThrow(() -> new AccessDeniedException("Você não foi convidado para esta reunião."));

        participant.setStatus(status);
        meetingParticipantRepository.save(participant);

        return meetingMapper.toDetailsDto(meeting);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeetingDetailsDto> findMyMeetings(String userEmail, int year, int month) {
        Person user = personRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay();

        return meetingRepository.findMeetingsByPersonAndPeriod(user.getId(), startOfMonth, endOfMonth).stream()
                .map(meetingMapper::toDetailsDto)
                .collect(Collectors.toList());
    }
}

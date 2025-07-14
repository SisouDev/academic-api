package com.institution.management.academic_api.domain.service.meeting;

import com.institution.management.academic_api.application.dto.meeting.CreateMeetingRequestDto;
import com.institution.management.academic_api.application.dto.meeting.MeetingDetailsDto;
import com.institution.management.academic_api.application.dto.meeting.UpdateMeetingRequestDto;
import com.institution.management.academic_api.domain.model.enums.meeting.MeetingParticipantStatus;

import java.util.List;

public interface MeetingService {
    MeetingDetailsDto create(CreateMeetingRequestDto dto, String organizerEmail);

    MeetingDetailsDto update(Long id, UpdateMeetingRequestDto dto);

    void delete(Long id);

    MeetingDetailsDto findById(Long id);

    List<MeetingDetailsDto> findMyMeetings(String userEmail, int year, int month);

    MeetingDetailsDto rsvp(Long meetingId, String userEmail, MeetingParticipantStatus status);

}

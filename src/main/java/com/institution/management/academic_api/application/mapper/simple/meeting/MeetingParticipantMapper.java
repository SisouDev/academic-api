package com.institution.management.academic_api.application.mapper.simple.meeting;

import com.institution.management.academic_api.application.dto.meeting.MeetingParticipantDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.meeting.MeetingParticipant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface MeetingParticipantMapper {
    @Mapping(target = "participant", source = "participant")
    @Mapping(target = "status", source = "status.displayName")
    MeetingParticipantDto toDto(MeetingParticipant meetingParticipant);
}
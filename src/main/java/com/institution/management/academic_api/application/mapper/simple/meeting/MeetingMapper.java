package com.institution.management.academic_api.application.mapper.simple.meeting;

import com.institution.management.academic_api.application.dto.meeting.CreateMeetingRequestDto;
import com.institution.management.academic_api.application.dto.meeting.MeetingDetailsDto;
import com.institution.management.academic_api.application.dto.meeting.MeetingSummaryDto;
import com.institution.management.academic_api.application.dto.meeting.UpdateMeetingRequestDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.meeting.Meeting;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PersonMapper.class, MeetingParticipantMapper.class})
public interface MeetingMapper {
    @Mapping(source = "organizer", target = "organizer")
    @Mapping(target = "participants", source = "participants")
    MeetingDetailsDto toDetailsDto(Meeting meeting);

    @Mapping(target = "organizerName", source = "organizer", qualifiedByName = "personToFullName")
    MeetingSummaryDto toSummaryDto(Meeting meeting);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "participants", ignore = true)
    Meeting toEntity(CreateMeetingRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "participants", ignore = true)
    void updateFromDto(UpdateMeetingRequestDto dto, @MappingTarget Meeting entity);
}
package com.institution.management.academic_api.application.mapper.simple.humanResources;

import com.institution.management.academic_api.application.dto.humanResources.CreateLeaveRequestDto;
import com.institution.management.academic_api.application.dto.humanResources.LeaveRequestDetailsDto;
import com.institution.management.academic_api.application.dto.humanResources.LeaveRequestSummaryDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.humanResources.LeaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface LeaveRequestMapper {
    @Mapping(target = "requester", source = "requester")
    @Mapping(target = "reviewer", source = "reviewer")
    @Mapping(target = "type", expression = "java(leaveRequest.getType().getDisplayName())")
    @Mapping(target = "status", expression = "java(leaveRequest.getStatus().getDisplayName())")
    LeaveRequestDetailsDto toDetailsDto(LeaveRequest leaveRequest);

    @Mapping(target = "requesterName", source = "requester", qualifiedByName = "personToFullName")
    @Mapping(target = "type", expression = "java(leaveRequest.getType().getDisplayName())")
    @Mapping(target = "status", expression = "java(leaveRequest.getStatus().getDisplayName())")
    LeaveRequestSummaryDto toSummaryDto(LeaveRequest leaveRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "reviewer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    LeaveRequest toEntity(CreateLeaveRequestDto dto);
}
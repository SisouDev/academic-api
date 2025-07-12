package com.institution.management.academic_api.application.mapper.simple.helpDesk;

import com.institution.management.academic_api.application.dto.helpDesk.CreateSupportTicketRequestDto;
import com.institution.management.academic_api.application.dto.helpDesk.SupportTicketDetailsDto;
import com.institution.management.academic_api.application.dto.helpDesk.SupportTicketSummaryDto;
import com.institution.management.academic_api.application.dto.helpDesk.UpdateSupportTicketRequestDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.helpDesk.SupportTicket;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface SupportTicketMapper {
    @Mapping(target = "status", expression = "java(ticket.getStatus().getDisplayName())")
    @Mapping(target = "priority", expression = "java(ticket.getPriority().getDisplayName())")
    @Mapping(target = "category", expression = "java(ticket.getCategory().getDisplayName())")
    SupportTicketDetailsDto toDetailsDto(SupportTicket ticket);

    @Mapping(target = "requesterName", source = "requester", qualifiedByName = "personToFullName")
    @Mapping(target = "status", expression = "java(ticket.getStatus().getDisplayName())")
    @Mapping(target = "priority", expression = "java(ticket.getPriority().getDisplayName())")
    @Mapping(target = "category", expression = "java(ticket.getCategory().getDisplayName())")
    SupportTicketSummaryDto toSummaryDto(SupportTicket ticket);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "resolvedAt", ignore = true)
    @Mapping(target = "closedAt", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    SupportTicket toEntity(CreateSupportTicketRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "resolvedAt", ignore = true)
    @Mapping(target = "closedAt", ignore = true)
    void updateFromDto(UpdateSupportTicketRequestDto dto, @MappingTarget SupportTicket entity);
}
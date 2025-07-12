package com.institution.management.academic_api.domain.factory.helpDesk;

import com.institution.management.academic_api.application.dto.helpDesk.CreateSupportTicketRequestDto;
import com.institution.management.academic_api.application.mapper.simple.helpDesk.SupportTicketMapper;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.helpDesk.SupportTicket;
import com.institution.management.academic_api.domain.model.enums.helpDesk.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SupportTicketFactory {

    private final SupportTicketMapper ticketMapper;

    public SupportTicket create(CreateSupportTicketRequestDto dto, Person requester) {
        SupportTicket ticket = ticketMapper.toEntity(dto);

        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setStatus(TicketStatus.OPEN);

        ticket.setRequester(requester);

        ticket.setAssignee(null);
        ticket.setResolvedAt(null);
        ticket.setClosedAt(null);

        return ticket;
    }
}
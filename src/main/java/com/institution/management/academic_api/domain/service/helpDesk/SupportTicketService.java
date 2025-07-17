package com.institution.management.academic_api.domain.service.helpDesk;

import com.institution.management.academic_api.application.dto.helpDesk.CreateSupportTicketRequestDto;
import com.institution.management.academic_api.application.dto.helpDesk.SupportTicketDetailsDto;
import com.institution.management.academic_api.application.dto.helpDesk.SupportTicketSummaryDto;
import com.institution.management.academic_api.application.dto.helpDesk.UpdateSupportTicketRequestDto;
import com.institution.management.academic_api.domain.model.enums.helpDesk.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupportTicketService {
    SupportTicketDetailsDto create(CreateSupportTicketRequestDto dto, String requesterEmail);

    void assignTicket(Long ticketId, Long assigneeId);

    SupportTicketDetailsDto update(Long ticketId, UpdateSupportTicketRequestDto dto);

    SupportTicketDetailsDto findById(Long id);

    List<SupportTicketDetailsDto> findByRequester(Long personId);

    List<SupportTicketSummaryDto> findAll();

    Page<SupportTicketDetailsDto> findAll(TicketStatus status, Long assigneeId, Pageable pageable);

}

package com.institution.management.academic_api.application.service.helpDesk;

import com.institution.management.academic_api.application.dto.helpDesk.CreateSupportTicketRequestDto;
import com.institution.management.academic_api.application.dto.helpDesk.SupportTicketDetailsDto;
import com.institution.management.academic_api.application.dto.helpDesk.SupportTicketSummaryDto;
import com.institution.management.academic_api.application.dto.helpDesk.UpdateSupportTicketRequestDto;
import com.institution.management.academic_api.application.mapper.simple.helpDesk.SupportTicketMapper;
import com.institution.management.academic_api.application.notifiers.helpDesk.SupportTicketNotifier;
import com.institution.management.academic_api.application.service.utils.RoundRobinAssignerService;
import com.institution.management.academic_api.domain.factory.helpDesk.SupportTicketFactory;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.helpDesk.SupportTicket;
import com.institution.management.academic_api.domain.model.entities.specification.SupportTicketSpecification;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import com.institution.management.academic_api.domain.model.enums.helpDesk.TicketStatus;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.helpDesk.SupportTicketRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import com.institution.management.academic_api.domain.service.helpDesk.SupportTicketService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportTicketServiceImpl implements SupportTicketService {

    private final SupportTicketRepository ticketRepository;
    private final PersonRepository personRepository;
    private final EmployeeRepository employeeRepository;
    private final SupportTicketFactory ticketFactory;
    private final SupportTicketMapper ticketMapper;
    private final NotificationService notificationService;
    private final SupportTicketNotifier ticketNotifier;
    private final RoundRobinAssignerService roundRobinAssigner;
    private final SupportTicketRepository supportTicketRepository;
    private final SupportTicketMapper supportTicketMapper;


    @Override
    @Transactional
    @LogActivity("Abriu um novo chamado de suporte.")
    public SupportTicketDetailsDto create(CreateSupportTicketRequestDto dto, String requesterEmail) {
        Person requester = personRepository.findByUser_Login(requesterEmail)
                .orElseThrow(() -> new EntityNotFoundException("Requisitante não encontrado com email: " + requesterEmail));

        SupportTicket newTicket = ticketFactory.create(dto, requester);

        Employee handler = roundRobinAssigner.getNextHandlerByJobPosition(JobPosition.TECHNICIAN);
        if (handler != null) {
            newTicket.setAssignee(handler);
            newTicket.setStatus(TicketStatus.IN_PROGRESS);
            ticketNotifier.notifyAssigneeOfNewTicket(newTicket);
        }

        SupportTicket savedTicket = ticketRepository.save(newTicket);
        ticketNotifier.notifySupportTeamOfNewTicket(savedTicket);
        return ticketMapper.toDetailsDto(savedTicket);
    }

    @Override
    @Transactional
    @LogActivity("Atribuiu um chamado de suporte.")
    public void assignTicket(Long ticketId, Long assigneeId) {
        SupportTicket ticket = findTicketByIdOrThrow(ticketId);

        Employee assignee = employeeRepository.findById(assigneeId)
                .orElseThrow(() -> new EntityNotFoundException("Technician not found with id: " + assigneeId));

        ticket.setAssignee(assignee);
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        ticketNotifier.notifyRequesterOfAssignment(ticket);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou um chamado de suporte.")
    public SupportTicketDetailsDto update(Long ticketId, UpdateSupportTicketRequestDto dto) {
        SupportTicket ticket = findTicketByIdOrThrow(ticketId);

        ticketMapper.updateFromDto(dto, ticket);

        TicketStatus newStatus = TicketStatus.valueOf(dto.status().toUpperCase());
        if (newStatus == TicketStatus.RESOLVED) {
            ticket.setResolvedAt(LocalDateTime.now());
            ticketNotifier.notifyRequesterOfResolution(ticket);
        } else if (newStatus == TicketStatus.CLOSED) {
            ticket.setClosedAt(LocalDateTime.now());
            ticketNotifier.notifyRequesterOfResolution(ticket);
        }

        return ticketMapper.toDetailsDto(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public SupportTicketDetailsDto findById(Long id) {
        return ticketRepository.findById(id)
                .map(ticketMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportTicketDetailsDto> findByRequester(Long personId) {
        return ticketRepository.findByRequesterId(personId).stream()
                .map(ticketMapper::toDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupportTicketDetailsDto> findAll(TicketStatus status, Long assigneeId, Pageable pageable) {
        Specification<SupportTicket> spec = SupportTicketSpecification.filterBy(status, assigneeId);
        return supportTicketRepository.findAll(spec, pageable)
                .map(supportTicketMapper::toDetailsDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportTicketSummaryDto> findAll() {
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    private SupportTicket findTicketByIdOrThrow(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));
    }
}

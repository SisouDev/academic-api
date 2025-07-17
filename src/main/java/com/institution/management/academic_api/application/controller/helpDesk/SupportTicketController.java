package com.institution.management.academic_api.application.controller.helpDesk;

import com.institution.management.academic_api.application.dto.helpDesk.CreateSupportTicketRequestDto;
import com.institution.management.academic_api.application.dto.helpDesk.SupportTicketDetailsDto;
import com.institution.management.academic_api.application.dto.helpDesk.UpdateSupportTicketRequestDto;
import com.institution.management.academic_api.domain.model.enums.helpDesk.TicketStatus;
import com.institution.management.academic_api.domain.service.helpDesk.SupportTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/support-tickets")
@RequiredArgsConstructor
@Tag(name = "Support Tickets", description = "Endpoints para gerenciamento de chamados de suporte técnico")
public class SupportTicketController {

    private final SupportTicketService ticketService;

    @PostMapping
    @Operation(summary = "Abre um novo chamado de suporte")
    public ResponseEntity<EntityModel<SupportTicketDetailsDto>> create(@RequestBody CreateSupportTicketRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requesterEmail = authentication.getName();
        SupportTicketDetailsDto createdTicket = ticketService.create(request, requesterEmail);
        EntityModel<SupportTicketDetailsDto> resource = EntityModel.of(createdTicket,
                linkTo(methodOn(SupportTicketController.class).findById(createdTicket.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @PatchMapping("/{ticketId}/assign/{assigneeId}")
    @Operation(summary = "Atribui um chamado a um técnico de suporte")
    public ResponseEntity<Void> assignTicket(@PathVariable Long ticketId, @PathVariable Long assigneeId) {
        ticketService.assignTicket(ticketId, assigneeId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{ticketId}")
    @Operation(summary = "Atualiza o status ou prioridade de um chamado")
    public ResponseEntity<EntityModel<SupportTicketDetailsDto>> update(@PathVariable Long ticketId, @RequestBody UpdateSupportTicketRequestDto request) {
        SupportTicketDetailsDto updatedTicket = ticketService.update(ticketId, request);
        EntityModel<SupportTicketDetailsDto> resource = EntityModel.of(updatedTicket,
                linkTo(methodOn(SupportTicketController.class).findById(ticketId)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um chamado de suporte pelo ID")
    public ResponseEntity<EntityModel<SupportTicketDetailsDto>> findById(@PathVariable Long id) {
        SupportTicketDetailsDto ticket = ticketService.findById(id);
        EntityModel<SupportTicketDetailsDto> resource = EntityModel.of(ticket,
                linkTo(methodOn(SupportTicketController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/my-tickets")
    @Operation(summary = "Busca todos os chamados abertos pelo usuário logado")
    public ResponseEntity<List<EntityModel<SupportTicketDetailsDto>>> findMyTickets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        List<SupportTicketDetailsDto> tickets = List.of();

        List<EntityModel<SupportTicketDetailsDto>> resources = tickets.stream()
                .map(ticket -> EntityModel.of(ticket,
                        linkTo(methodOn(SupportTicketController.class).findById(ticket.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TECHNICIAN', 'ADMIN', 'MANAGER')")
    @Operation(summary = "Lista todos os chamados de suporte com filtros")
    public ResponseEntity<PagedModel<EntityModel<SupportTicketDetailsDto>>> findAll(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) Long assigneeId,
            Pageable pageable,
            PagedResourcesAssembler<SupportTicketDetailsDto> assembler) {

        Page<SupportTicketDetailsDto> ticketsPage = ticketService.findAll(status, assigneeId, pageable);
        return ResponseEntity.ok(assembler.toModel(ticketsPage));
    }
}
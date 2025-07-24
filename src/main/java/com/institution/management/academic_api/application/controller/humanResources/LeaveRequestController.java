package com.institution.management.academic_api.application.controller.humanResources;

import com.institution.management.academic_api.application.dto.humanResources.CreateLeaveRequestDto;
import com.institution.management.academic_api.application.dto.humanResources.LeaveRequestDetailsDto;
import com.institution.management.academic_api.application.dto.humanResources.ReviewLeaveRequestDto;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.enums.humanResources.LeaveRequestStatus;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.service.humanResources.LeaveRequestService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
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
@RequestMapping("/api/v1/leave-requests")
@RequiredArgsConstructor
@Tag(name = "Leave Requests", description = "Endpoints para gerenciamento de solicitações de ausência (férias, folgas)")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final EmployeeRepository employeeRepository;

    @PostMapping
    @Operation(summary = "Cria uma nova solicitação de ausência")
    public ResponseEntity<EntityModel<LeaveRequestDetailsDto>> create(@RequestBody CreateLeaveRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requesterEmail = authentication.getName();
        LeaveRequestDetailsDto createdRequest = leaveRequestService.create(request, requesterEmail);
        EntityModel<LeaveRequestDetailsDto> resource = EntityModel.of(createdRequest,
                linkTo(methodOn(LeaveRequestController.class).findById(createdRequest.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @PatchMapping("/{id}/review")
    @Operation(summary = "Aprova ou rejeita uma solicitação de ausência")
    public ResponseEntity<Void> review(@PathVariable Long id, @RequestBody ReviewLeaveRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String reviewerEmail = authentication.getName();
        leaveRequestService.review(id, request, reviewerEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('HR_ANALYST', 'MANAGER', 'DIRECTOR')")
    @Operation(summary = "Lista todas as solicitações de ausência com filtros")
    public ResponseEntity<PagedModel<EntityModel<LeaveRequestDetailsDto>>> findAll(
            @RequestParam(required = false) LeaveRequestStatus status,
            Pageable pageable,
            PagedResourcesAssembler<LeaveRequestDetailsDto> assembler) {

        Page<LeaveRequestDetailsDto> requestsPage = leaveRequestService.findAll(status, pageable);
        PagedModel<EntityModel<LeaveRequestDetailsDto>> pagedModel = assembler.toModel(requestsPage,
                request -> EntityModel.of(request,
                        linkTo(methodOn(LeaveRequestController.class).findById(request.id())).withSelfRel()));
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma solicitação de ausência pelo ID")
    public ResponseEntity<EntityModel<LeaveRequestDetailsDto>> findById(@PathVariable Long id) {
        LeaveRequestDetailsDto leaveRequest = leaveRequestService.findById(id);
        EntityModel<LeaveRequestDetailsDto> resource = EntityModel.of(leaveRequest,
                linkTo(methodOn(LeaveRequestController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/my-requests")
    @Operation(summary = "Lista todas as solicitações de ausência do usuário logado")
    public ResponseEntity<CollectionModel<EntityModel<LeaveRequestDetailsDto>>> findMyRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Employee requester = employeeRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + userEmail));

        List<LeaveRequestDetailsDto> requests = leaveRequestService.findByRequester(requester.getId());
        List<EntityModel<LeaveRequestDetailsDto>> resources = requests.stream()
                .map(request -> EntityModel.of(request,
                        linkTo(methodOn(LeaveRequestController.class).findById(request.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resources,
                linkTo(methodOn(LeaveRequestController.class).findMyRequests()).withSelfRel()));
    }
}
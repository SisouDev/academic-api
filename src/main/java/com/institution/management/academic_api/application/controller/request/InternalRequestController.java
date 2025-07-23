package com.institution.management.academic_api.application.controller.request;

import com.institution.management.academic_api.application.dto.request.CreateInternalRequestDto;
import com.institution.management.academic_api.application.dto.request.InternalRequestDetailsDto;
import com.institution.management.academic_api.application.dto.request.UpdateInternalRequestDto;
import com.institution.management.academic_api.domain.model.enums.request.RequestStatus;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.service.request.InternalRequestService;
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
@RequestMapping("/api/v1/internal-requests")
@RequiredArgsConstructor
@Tag(name = "Internal Requests", description = "Endpoints para gerenciamento de requisições internas")
public class InternalRequestController {

    private final InternalRequestService requestService;
    private final PersonRepository personRepository;
    private final PagedResourcesAssembler<InternalRequestDetailsDto> assembler;


    @PostMapping
    @Operation(summary = "Cria uma nova requisição interna")
    public ResponseEntity<EntityModel<InternalRequestDetailsDto>> create(@RequestBody CreateInternalRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requesterEmail = authentication.getName();
        InternalRequestDetailsDto createdRequest = requestService.create(request, requesterEmail);
        EntityModel<InternalRequestDetailsDto> resource = EntityModel.of(createdRequest,
                linkTo(methodOn(InternalRequestController.class).findById(createdRequest.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @PatchMapping("/{requestId}/assign/{handlerId}")
    @Operation(summary = "Atribui uma requisição a um funcionário responsável")
    public ResponseEntity<Void> assignRequest(@PathVariable Long requestId, @PathVariable Long handlerId) {
        requestService.assignRequest(requestId, handlerId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{requestId}/status")
    @Operation(summary = "Atualiza o status de uma requisição")
    public ResponseEntity<EntityModel<InternalRequestDetailsDto>> updateStatus(@PathVariable Long requestId, @RequestBody UpdateInternalRequestDto request) {
        InternalRequestDetailsDto updatedRequest = requestService.updateStatus(requestId, request);
        EntityModel<InternalRequestDetailsDto> resource = EntityModel.of(updatedRequest,
                linkTo(methodOn(InternalRequestController.class).findById(requestId)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma requisição interna pelo ID")
    public ResponseEntity<EntityModel<InternalRequestDetailsDto>> findById(@PathVariable Long id) {
        InternalRequestDetailsDto request = requestService.findById(id);
        EntityModel<InternalRequestDetailsDto> resource = EntityModel.of(request,
                linkTo(methodOn(InternalRequestController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/my-requests")
    @Operation(summary = "Lista todas as requisições internas criadas pelo usuário logado")
    public ResponseEntity<CollectionModel<EntityModel<InternalRequestDetailsDto>>> findMyRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        List<InternalRequestDetailsDto> requests = requestService.findMyRequests(userEmail);

        List<EntityModel<InternalRequestDetailsDto>> resources = requests.stream()
                .map(request -> EntityModel.of(request,
                        linkTo(methodOn(InternalRequestController.class).findById(request.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resources,
                linkTo(methodOn(InternalRequestController.class).findMyRequests()).withSelfRel()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SECRETARY', 'ADMIN')")
    @Operation(summary = "Lista todas as requisições internas com filtro de status")
    public ResponseEntity<PagedModel<EntityModel<InternalRequestDetailsDto>>> findAll(
            @RequestParam(required = false) RequestStatus status,
            Pageable pageable) {

        Page<InternalRequestDetailsDto> page = requestService.findAll(status, pageable);

        return ResponseEntity.ok(assembler.toModel(page,
                request -> EntityModel.of(request,
                        linkTo(methodOn(InternalRequestController.class).findById(request.id())).withSelfRel())));
    }
}
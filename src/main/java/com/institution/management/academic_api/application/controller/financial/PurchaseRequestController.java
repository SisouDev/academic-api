package com.institution.management.academic_api.application.controller.financial;

import com.institution.management.academic_api.application.dto.financial.CreatePurchaseRequestDto;
import com.institution.management.academic_api.application.dto.financial.PurchaseRequestDto;
import com.institution.management.academic_api.domain.model.enums.financial.PurchaseRequestStatus;
import com.institution.management.academic_api.domain.service.financial.PurchaseRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/purchase-requests")
@RequiredArgsConstructor
@Tag(name = "Purchase Requests", description = "Endpoints para o fluxo de requisição de compras simples")
public class PurchaseRequestController {

    private final PurchaseRequestService requestService;
    private final PagedResourcesAssembler<PurchaseRequestDto> assembler;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Cria uma nova requisição de compra simples")
    public ResponseEntity<EntityModel<PurchaseRequestDto>> create(@Valid @RequestBody CreatePurchaseRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requesterEmail = authentication.getName();

        PurchaseRequestDto createdRequest = requestService.create(request, requesterEmail);
        EntityModel<PurchaseRequestDto> resource = EntityModel.of(createdRequest,
                linkTo(methodOn(PurchaseRequestController.class).findById(createdRequest.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('FINANCE_ASSISTANT', 'FINANCE_MANAGER', 'ADMIN')")
    @Operation(summary = "Busca uma requisição de compra pelo ID")
    public ResponseEntity<EntityModel<PurchaseRequestDto>> findById(@PathVariable Long id) {
        PurchaseRequestDto request = requestService.findById(id);
        EntityModel<PurchaseRequestDto> resource = EntityModel.of(request,
                linkTo(methodOn(PurchaseRequestController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('FINANCE_ASSISTANT', 'FINANCE_MANAGER', 'ADMIN')")
    @Operation(summary = "Lista todas as requisições de compra com filtros")
    public ResponseEntity<PagedModel<EntityModel<PurchaseRequestDto>>> findAll(
            @RequestParam(required = false) PurchaseRequestStatus status,
            Pageable pageable) {

        Page<PurchaseRequestDto> page = requestService.findAll(status, pageable);
        return ResponseEntity.ok(assembler.toModel(page,
                request -> EntityModel.of(request, linkTo(methodOn(PurchaseRequestController.class).findById(request.id())).withSelfRel())));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('FINANCE_ASSISTANT', 'FINANCE_MANAGER', 'ADMIN')")
    @Operation(summary = "Atualiza o status de uma requisição (usado pelo assistente para aprovar/rejeitar)")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam PurchaseRequestStatus newStatus) {
        requestService.updateStatus(id, newStatus);
        return ResponseEntity.noContent().build();
    }
}
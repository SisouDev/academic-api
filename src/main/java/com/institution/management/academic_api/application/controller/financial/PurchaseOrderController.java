package com.institution.management.academic_api.application.controller.financial;

import com.institution.management.academic_api.application.dto.financial.CreatePurchaseOrderRequestDto;
import com.institution.management.academic_api.application.dto.financial.PurchaseOrderDto;
import com.institution.management.academic_api.domain.model.enums.financial.OrderStatus;
import com.institution.management.academic_api.domain.service.financial.PurchaseOrderService;
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
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
@Tag(name = "Purchase Orders", description = "Endpoints para gerenciamento de ordens de compra (Contas a Pagar)")
public class PurchaseOrderController {

    private final PurchaseOrderService poService;
    private final PagedResourcesAssembler<PurchaseOrderDto> assembler;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Cria uma nova ordem de compra")
    public ResponseEntity<EntityModel<PurchaseOrderDto>> create(@Valid @RequestBody CreatePurchaseOrderRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requesterEmail = authentication.getName();
        PurchaseOrderDto createdPO = poService.create(request, requesterEmail);
        EntityModel<PurchaseOrderDto> resource = EntityModel.of(createdPO,
                linkTo(methodOn(PurchaseOrderController.class).findById(createdPO.id())).withSelfRel());
        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('FINANCE_ASSISTANT', 'FINANCE_MANAGER', 'ADMIN')")
    @Operation(summary = "Busca uma ordem de compra pelo ID")
    public ResponseEntity<EntityModel<PurchaseOrderDto>> findById(@PathVariable Long id) {
        PurchaseOrderDto po = poService.findById(id);
        EntityModel<PurchaseOrderDto> resource = EntityModel.of(po,
                linkTo(methodOn(PurchaseOrderController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('FINANCE_ASSISTANT', 'FINANCE_MANAGER', 'ADMIN')")
    @Operation(summary = "Lista todas as ordens de compra com filtros")
    public ResponseEntity<PagedModel<EntityModel<PurchaseOrderDto>>> findAll(@RequestParam(required = false) OrderStatus status, Pageable pageable) {
        Page<PurchaseOrderDto> page = poService.findAll(status, pageable);
        return ResponseEntity.ok(assembler.toModel(page,
                po -> EntityModel.of(po, linkTo(methodOn(PurchaseOrderController.class).findById(po.id())).withSelfRel())));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('FINANCE_MANAGER', 'ADMIN')")
    @Operation(summary = "Atualiza o status de uma ordem de compra (aprovar, pagar, etc.)")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        poService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}
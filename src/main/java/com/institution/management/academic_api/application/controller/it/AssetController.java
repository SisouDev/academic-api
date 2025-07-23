package com.institution.management.academic_api.application.controller.it;

import com.institution.management.academic_api.application.dto.it.AssetDetailsDto;
import com.institution.management.academic_api.application.dto.it.CreateAssetRequestDto;
import com.institution.management.academic_api.domain.model.enums.it.AssetStatus;
import com.institution.management.academic_api.domain.service.it.AssetService;
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
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/it/assets")
@RequiredArgsConstructor
@Tag(name = "IT Assets", description = "Endpoints para gerenciamento de ativos de TI")
public class AssetController {

    private final AssetService assetService;

    @GetMapping
    @PreAuthorize("hasAnyRole('TECHNICIAN', 'ADMIN', 'MANAGER')")
    @Operation(summary = "Lista todos os ativos de TI com filtros")
    public ResponseEntity<PagedModel<EntityModel<AssetDetailsDto>>> findAll(
            @RequestParam(required = false) AssetStatus status,
            @RequestParam(required = false) Long assignedToId,
            Pageable pageable,
            PagedResourcesAssembler<AssetDetailsDto> assembler) {

        Page<AssetDetailsDto> assetsPage = assetService.findAll(status, assignedToId, pageable);
        return ResponseEntity.ok(assembler.toModel(assetsPage));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TECHNICIAN', 'ADMIN')")
    @Operation(summary = "Registra um novo ativo de TI no sistema")
    public ResponseEntity<EntityModel<AssetDetailsDto>> create(@Valid @RequestBody CreateAssetRequestDto request) {
        AssetDetailsDto createdAsset = assetService.create(request);

        EntityModel<AssetDetailsDto> resource = EntityModel.of(createdAsset,
                linkTo(methodOn(AssetController.class).findById(createdAsset.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TECHNICIAN', 'ADMIN')")
    @Operation(summary = "Busca um ativo de TI pelo ID")
    public ResponseEntity<EntityModel<AssetDetailsDto>> findById(@PathVariable Long id) {
        AssetDetailsDto asset = assetService.findById(id);
        EntityModel<AssetDetailsDto> resource = EntityModel.of(asset,
                linkTo(methodOn(AssetController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

}
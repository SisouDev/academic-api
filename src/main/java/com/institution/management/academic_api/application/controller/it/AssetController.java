package com.institution.management.academic_api.application.controller.it;

import com.institution.management.academic_api.application.dto.it.AssetDetailsDto;
import com.institution.management.academic_api.domain.model.enums.it.AssetStatus;
import com.institution.management.academic_api.domain.service.it.AssetService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
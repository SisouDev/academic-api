package com.institution.management.academic_api.application.controller.inventory;

import com.institution.management.academic_api.application.dto.inventory.CreateMaterialRequestDto;
import com.institution.management.academic_api.application.dto.inventory.MaterialDetailsDto;
import com.institution.management.academic_api.application.dto.inventory.UpdateMaterialRequestDto;
import com.institution.management.academic_api.domain.service.inventory.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/materials")
@RequiredArgsConstructor
@Tag(name = "Materials", description = "Endpoints para gerenciamento do catálogo de materiais")
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    @Operation(summary = "Cria um novo material no catálogo")
    public ResponseEntity<EntityModel<MaterialDetailsDto>> create(@RequestBody CreateMaterialRequestDto request) {
        MaterialDetailsDto createdMaterial = materialService.create(request);
        EntityModel<MaterialDetailsDto> resource = EntityModel.of(createdMaterial,
                linkTo(methodOn(MaterialController.class).findById(createdMaterial.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza um material existente")
    public ResponseEntity<EntityModel<MaterialDetailsDto>> update(@PathVariable Long id, @RequestBody UpdateMaterialRequestDto request) {
        MaterialDetailsDto updatedMaterial = materialService.update(id, request);
        EntityModel<MaterialDetailsDto> resource = EntityModel.of(updatedMaterial,
                linkTo(methodOn(MaterialController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um material do catálogo")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um material pelo ID")
    public ResponseEntity<EntityModel<MaterialDetailsDto>> findById(@PathVariable Long id) {
        MaterialDetailsDto material = materialService.findById(id);
        EntityModel<MaterialDetailsDto> resource = EntityModel.of(material,
                linkTo(methodOn(MaterialController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(MaterialController.class).findAll()).withRel("all-materials"));
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Lista todos os materiais do catálogo")
    public ResponseEntity<CollectionModel<EntityModel<MaterialDetailsDto>>> findAll() {
        List<MaterialDetailsDto> materials = materialService.findAll();

        List<EntityModel<MaterialDetailsDto>> resources = materials.stream()
                .map(material -> EntityModel.of(material,
                        linkTo(methodOn(MaterialController.class).findById(material.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resources,
                linkTo(methodOn(MaterialController.class).findAll()).withSelfRel()));
    }
}
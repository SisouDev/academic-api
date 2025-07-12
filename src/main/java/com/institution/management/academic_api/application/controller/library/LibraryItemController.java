package com.institution.management.academic_api.application.controller.library;

import com.institution.management.academic_api.application.dto.library.CreateLibraryItemRequestDto;
import com.institution.management.academic_api.application.dto.library.LibraryItemDetailsDto;
import com.institution.management.academic_api.application.dto.library.UpdateLibraryItemRequestDto;
import com.institution.management.academic_api.domain.service.library.LibraryItemService;
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
@RequestMapping("/api/v1/library/items")
@RequiredArgsConstructor
@Tag(name = "Library Items", description = "Endpoints para gerenciamento do acervo da biblioteca")
public class LibraryItemController {

    private final LibraryItemService libraryItemService;

    @PostMapping
    @Operation(summary = "Adiciona um novo item ao acervo")
    public ResponseEntity<EntityModel<LibraryItemDetailsDto>> create(@RequestBody CreateLibraryItemRequestDto request) {
        LibraryItemDetailsDto createdItem = libraryItemService.create(request);
        EntityModel<LibraryItemDetailsDto> resource = EntityModel.of(createdItem,
                linkTo(methodOn(LibraryItemController.class).findById(createdItem.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza um item existente no acervo")
    public ResponseEntity<EntityModel<LibraryItemDetailsDto>> update(@PathVariable Long id, @RequestBody UpdateLibraryItemRequestDto request) {
        LibraryItemDetailsDto updatedItem = libraryItemService.update(id, request);
        EntityModel<LibraryItemDetailsDto> resource = EntityModel.of(updatedItem,
                linkTo(methodOn(LibraryItemController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um item do acervo")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        libraryItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca os detalhes de um item do acervo pelo ID")
    public ResponseEntity<EntityModel<LibraryItemDetailsDto>> findById(@PathVariable Long id) {
        LibraryItemDetailsDto item = libraryItemService.findById(id);
        EntityModel<LibraryItemDetailsDto> resource = EntityModel.of(item,
                linkTo(methodOn(LibraryItemController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(LibraryItemController.class).findAll()).withRel("all-items"));
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Lista todos os itens disponíveis no acervo")
    public ResponseEntity<CollectionModel<EntityModel<LibraryItemDetailsDto>>> findAll() {
        List<LibraryItemDetailsDto> items = libraryItemService.findAll();

        List<EntityModel<LibraryItemDetailsDto>> resources = items.stream()
                .map(item -> EntityModel.of(item,
                        linkTo(methodOn(LibraryItemController.class).findById(item.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resources,
                linkTo(methodOn(LibraryItemController.class).findAll()).withSelfRel()));
    }
}
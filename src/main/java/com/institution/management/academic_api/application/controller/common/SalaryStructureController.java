package com.institution.management.academic_api.application.controller.common;

import com.institution.management.academic_api.application.dto.common.CreateSalaryStructureRequestDto;
import com.institution.management.academic_api.application.dto.common.SalaryStructureDto;
import com.institution.management.academic_api.application.dto.common.UpdateSalaryStructureRequestDto;
import com.institution.management.academic_api.domain.service.common.SalaryStructureService;
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
@RequestMapping("/api/v1/salary-structures")
@RequiredArgsConstructor
@Tag(name = "Salary Structures", description = "Endpoints para gerenciamento da estrutura salarial")
@PreAuthorize("hasAnyRole('ADMIN', 'HR_ANALYST')")
public class SalaryStructureController {

    private final SalaryStructureService salaryStructureService;
    private final PagedResourcesAssembler<SalaryStructureDto> assembler;

    @PostMapping
    @Operation(summary = "Cria uma nova regra de estrutura salarial")
    public ResponseEntity<EntityModel<SalaryStructureDto>> create(@Valid @RequestBody CreateSalaryStructureRequestDto request) {
        SalaryStructureDto created = salaryStructureService.create(request);
        EntityModel<SalaryStructureDto> resource = EntityModel.of(created,
                linkTo(methodOn(SalaryStructureController.class).findById(created.id())).withSelfRel());
        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma regra salarial pelo ID")
    public ResponseEntity<EntityModel<SalaryStructureDto>> findById(@PathVariable Long id) {
        SalaryStructureDto dto = salaryStructureService.findById(id);
        EntityModel<SalaryStructureDto> resource = EntityModel.of(dto,
                linkTo(methodOn(SalaryStructureController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Lista todas as regras de estrutura salarial")
    public ResponseEntity<PagedModel<EntityModel<SalaryStructureDto>>> findAll(Pageable pageable) {
        Page<SalaryStructureDto> page = salaryStructureService.findAll(pageable);
        return ResponseEntity.ok(assembler.toModel(page,
                structure -> EntityModel.of(structure, linkTo(methodOn(SalaryStructureController.class).findById(structure.id())).withSelfRel())));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o salário base de uma regra existente")
    public ResponseEntity<EntityModel<SalaryStructureDto>> update(@PathVariable Long id, @Valid @RequestBody UpdateSalaryStructureRequestDto request) {
        SalaryStructureDto updated = salaryStructureService.update(id, request);
        EntityModel<SalaryStructureDto> resource = EntityModel.of(updated,
                linkTo(methodOn(SalaryStructureController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma regra de estrutura salarial")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salaryStructureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
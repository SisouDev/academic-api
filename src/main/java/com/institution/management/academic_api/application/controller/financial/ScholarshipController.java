package com.institution.management.academic_api.application.controller.financial;

import com.institution.management.academic_api.application.dto.financial.CreateScholarshipRequestDto;
import com.institution.management.academic_api.application.dto.financial.ScholarshipDetailsDto;
import com.institution.management.academic_api.domain.model.enums.financial.ScholarshipStatus;
import com.institution.management.academic_api.domain.service.financial.ScholarshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/scholarships")
@RequiredArgsConstructor
@Tag(name = "Scholarships", description = "Endpoints para gerenciamento de bolsas de estudo")
@PreAuthorize("hasAnyRole('FINANCE_ASSISTANT', 'FINANCE_MANAGER', 'ADMIN')")
public class ScholarshipController {

    private final ScholarshipService scholarshipService;

    @PostMapping
    @Operation(summary = "Cria uma nova bolsa de estudos para uma matrícula")
    public ResponseEntity<EntityModel<ScholarshipDetailsDto>> create(@Valid @RequestBody CreateScholarshipRequestDto request) {
        ScholarshipDetailsDto createdScholarship = scholarshipService.create(request);
        EntityModel<ScholarshipDetailsDto> resource = EntityModel.of(createdScholarship,
                linkTo(methodOn(ScholarshipController.class).findById(createdScholarship.id())).withSelfRel());
        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma bolsa de estudos pelo ID")
    public ResponseEntity<EntityModel<ScholarshipDetailsDto>> findById(@PathVariable Long id) {
        ScholarshipDetailsDto scholarship = scholarshipService.findById(id);
        EntityModel<ScholarshipDetailsDto> resource = EntityModel.of(scholarship,
                linkTo(methodOn(ScholarshipController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Busca todas as bolsas de uma matrícula específica")
    public ResponseEntity<CollectionModel<EntityModel<ScholarshipDetailsDto>>> findByEnrollment(@RequestParam Long enrollmentId) {
        List<ScholarshipDetailsDto> scholarships = scholarshipService.findByEnrollment(enrollmentId);
        List<EntityModel<ScholarshipDetailsDto>> resources = scholarships.stream()
                .map(s -> EntityModel.of(s, linkTo(methodOn(ScholarshipController.class).findById(s.id())).withSelfRel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(resources,
                linkTo(methodOn(ScholarshipController.class).findByEnrollment(enrollmentId)).withSelfRel()));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza o status de uma bolsa (ex: de ACTIVE para INACTIVE)")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam ScholarshipStatus status) {
        scholarshipService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}
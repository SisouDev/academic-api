package com.institution.management.academic_api.application.controller.academic;

import com.institution.management.academic_api.application.dto.academic.AcademicTermDetailsDto;
import com.institution.management.academic_api.application.dto.academic.AcademicTermRequestDto;
import com.institution.management.academic_api.application.dto.academic.AcademicTermSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateAcademicTermRequestDto;
import com.institution.management.academic_api.domain.service.academic.AcademicTermService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/academic-terms")
@RequiredArgsConstructor
public class AcademicTermController {

    private final AcademicTermService academicTermService;
    private final PagedResourcesAssembler<AcademicTermSummaryDto> pagedAssembler;

    @PostMapping
    public ResponseEntity<AcademicTermDetailsDto> create(@RequestBody @Valid AcademicTermRequestDto request) {
        AcademicTermDetailsDto createdTerm = academicTermService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTerm);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AcademicTermDetailsDto>> findById(@PathVariable Long id) {
        AcademicTermDetailsDto term = academicTermService.findById(id);
        EntityModel<AcademicTermDetailsDto> model = EntityModel.of(term,
                linkTo(methodOn(AcademicTermController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<AcademicTermSummaryDto>>> findPaginated(
            @RequestParam(required = false) Long institutionId, Pageable pageable) {

        Page<AcademicTermSummaryDto> termsPage = academicTermService.findPaginated(institutionId, pageable);

        PagedModel<EntityModel<AcademicTermSummaryDto>> pagedModel = pagedAssembler.toModel(termsPage,
                term -> EntityModel.of(term,
                        linkTo(methodOn(AcademicTermController.class).findById(term.id())).withSelfRel()));

        return ResponseEntity.ok(pagedModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicTermDetailsDto> update(@PathVariable Long id, @RequestBody @Valid UpdateAcademicTermRequestDto request) {
        AcademicTermDetailsDto updatedTerm = academicTermService.update(id, request);
        return ResponseEntity.ok(updatedTerm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        academicTermService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
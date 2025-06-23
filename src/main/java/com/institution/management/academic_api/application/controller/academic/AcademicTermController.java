package com.institution.management.academic_api.application.controller.academic;

import com.institution.management.academic_api.application.controller.course.CourseSectionController;
import com.institution.management.academic_api.application.controller.institution.InstitutionController;
import com.institution.management.academic_api.application.dto.academic.AcademicTermDetailsDto;
import com.institution.management.academic_api.application.dto.academic.AcademicTermRequestDto;
import com.institution.management.academic_api.application.dto.academic.AcademicTermSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateAcademicTermRequestDto;
import com.institution.management.academic_api.domain.service.academic.AcademicTermService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/academic-terms")
@RequiredArgsConstructor
public class AcademicTermController {

    private final AcademicTermService academicTermService;

    @PostMapping
    public ResponseEntity<AcademicTermDetailsDto> create(@RequestBody @Valid AcademicTermRequestDto request) {
        AcademicTermDetailsDto createdTerm = academicTermService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTerm);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AcademicTermDetailsDto>> findById(@PathVariable Long id) {
        AcademicTermDetailsDto term = academicTermService.findById(id);

        EntityModel<AcademicTermDetailsDto> termModel = EntityModel.of(term,
                linkTo(methodOn(AcademicTermController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(CourseSectionController.class).findAllByAcademicTerm(id)).withRel("course-sections"),
                linkTo(methodOn(InstitutionController.class).findById(term.institution().id())).withRel("institution")
        );

        return ResponseEntity.ok(termModel);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<AcademicTermSummaryDto>>> findAllByInstitution(@RequestParam Long institutionId) {
        List<AcademicTermSummaryDto> terms = academicTermService.findAllByInstitution(institutionId);

        List<EntityModel<AcademicTermSummaryDto>> termModels = terms.stream()
                .map(term -> EntityModel.of(term,
                        linkTo(methodOn(AcademicTermController.class).findById(term.id())).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<AcademicTermSummaryDto>> collectionModel = CollectionModel.of(termModels,
                linkTo(methodOn(AcademicTermController.class).findAllByInstitution(institutionId)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
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
package com.institution.management.academic_api.application.controller.student;

import com.institution.management.academic_api.application.dto.student.AssessmentDefinitionDto;
import com.institution.management.academic_api.application.dto.student.CreateAssessmentDefinitionRequestDto;
import com.institution.management.academic_api.application.dto.student.UpdateAssessmentDefinitionRequestDto;
import com.institution.management.academic_api.domain.service.student.AssessmentDefinitionService;
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
@RequestMapping("/api/v1/assessment-definitions")
@RequiredArgsConstructor
public class AssessmentDefinitionController {

    private final AssessmentDefinitionService service;

    @PostMapping
    public ResponseEntity<EntityModel<AssessmentDefinitionDto>> create(@RequestBody @Valid CreateAssessmentDefinitionRequestDto request) {
        AssessmentDefinitionDto dto = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(EntityModel.of(dto, linkTo(methodOn(AssessmentDefinitionController.class).findById(dto.id())).withSelfRel()));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<AssessmentDefinitionDto>>> findAllByCourseSection(@RequestParam Long courseSectionId) {
        List<AssessmentDefinitionDto> dtos = service.findByCourseSection(courseSectionId);
        List<EntityModel<AssessmentDefinitionDto>> models = dtos.stream()
                .map(dto -> EntityModel.of(dto, linkTo(methodOn(AssessmentDefinitionController.class).findById(dto.id())).withSelfRel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models, linkTo(methodOn(AssessmentDefinitionController.class).findAllByCourseSection(courseSectionId)).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AssessmentDefinitionDto>> findById(@PathVariable Long id) {
        AssessmentDefinitionDto dto = service.findById(id);
        return ResponseEntity.ok(EntityModel.of(dto, linkTo(methodOn(AssessmentDefinitionController.class).findById(id)).withSelfRel()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<AssessmentDefinitionDto>> update(@PathVariable Long id, @RequestBody @Valid UpdateAssessmentDefinitionRequestDto request) {
        AssessmentDefinitionDto dto = service.update(id, request);
        return ResponseEntity.ok(EntityModel.of(dto, linkTo(methodOn(AssessmentDefinitionController.class).findById(id)).withSelfRel()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
package com.institution.management.academic_api.application.controller.academic;

import com.institution.management.academic_api.application.dto.academic.DepartmentDetailsDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentRequestDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateDepartmentRequestDto;
import com.institution.management.academic_api.domain.service.academic.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedResourcesAssembler;
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
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;
    private final PagedResourcesAssembler<DepartmentSummaryDto> pagedAssembler;

    @GetMapping("/all-for-selection")
    public ResponseEntity<CollectionModel<EntityModel<DepartmentSummaryDto>>> findAll() {
        List<DepartmentSummaryDto> departments = departmentService.findAllForSelection();
        List<EntityModel<DepartmentSummaryDto>> departmentModels = departments.stream()
                .map(dept -> EntityModel.of(dept,
                        linkTo(methodOn(DepartmentController.class).findById(dept.id())).withSelfRel()
                )).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(departmentModels,
                linkTo(methodOn(DepartmentController.class).findAll()).withSelfRel()
        ));
    }


    @GetMapping("/{id:\\\\d+}")
    public ResponseEntity<EntityModel<DepartmentDetailsDto>> findById(@PathVariable Long id) {
        DepartmentDetailsDto department = departmentService.findById(id);
        EntityModel<DepartmentDetailsDto> model = EntityModel.of(department,
                linkTo(methodOn(DepartmentController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @GetMapping("/by-institution")
    public ResponseEntity<CollectionModel<EntityModel<DepartmentSummaryDto>>> findAllByInstitution(@RequestParam Long institutionId) {
        List<DepartmentSummaryDto> departmentList = departmentService.findAllByInstitution(institutionId);

        List<EntityModel<DepartmentSummaryDto>> departmentModels = departmentList.stream()
                .map(dept -> EntityModel.of(dept,
                        linkTo(methodOn(DepartmentController.class).findById(dept.id())).withSelfRel()))
                .toList();

        var selfLink = linkTo(methodOn(DepartmentController.class).findAllByInstitution(institutionId)).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(departmentModels, selfLink));
    }

    @PostMapping
    public ResponseEntity<EntityModel<DepartmentDetailsDto>> create(@RequestBody @Valid DepartmentRequestDto request) {
        DepartmentDetailsDto created = departmentService.create(request);
        EntityModel<DepartmentDetailsDto> model = EntityModel.of(created,
                linkTo(methodOn(DepartmentController.class).findById(created.id())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<DepartmentDetailsDto>> update(@PathVariable Long id, @RequestBody @Valid UpdateDepartmentRequestDto request) {
        DepartmentDetailsDto updated = departmentService.update(id, request);
        EntityModel<DepartmentDetailsDto> model = EntityModel.of(updated,
                linkTo(methodOn(DepartmentController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EntityModel<DepartmentSummaryDto>>> findAllForCurrentUser() {
        List<DepartmentSummaryDto> departments = departmentService.findAllForCurrentUser();
        List<EntityModel<DepartmentSummaryDto>> departmentModels = departments.stream()
                .map(dept -> EntityModel.of(dept,
                        linkTo(methodOn(DepartmentController.class).findById(dept.id())).withSelfRel()))
                .collect(Collectors.toList());

        var selfLink = linkTo(methodOn(DepartmentController.class).findAllForCurrentUser()).withSelfRel();
        CollectionModel<EntityModel<DepartmentSummaryDto>> collectionModel = CollectionModel.of(departmentModels, selfLink);
        return ResponseEntity.ok(collectionModel);
    }
}
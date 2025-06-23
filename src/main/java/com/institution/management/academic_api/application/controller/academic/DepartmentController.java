package com.institution.management.academic_api.application.controller.academic;

import com.institution.management.academic_api.application.dto.academic.DepartmentDetailsDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentRequestDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateDepartmentRequestDto;
import com.institution.management.academic_api.domain.service.academic.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentDetailsDto> create(@RequestBody @Valid DepartmentRequestDto request) {
        DepartmentDetailsDto createdDepartment = departmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<DepartmentDetailsDto>> findById(@PathVariable Long id) {
        DepartmentDetailsDto departmentDto = departmentService.findById(id);

        var selfLink = linkTo(methodOn(DepartmentController.class).findById(id)).withSelfRel();
        EntityModel<DepartmentDetailsDto> model = EntityModel.of(departmentDto, selfLink /*, coursesLink */);

        return ResponseEntity.ok(model);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<DepartmentSummaryDto>>> findAllByInstitution(@RequestParam Long institutionId) {
        List<DepartmentSummaryDto> departmentList = departmentService.findAllByInstitution(institutionId);

        List<EntityModel<DepartmentSummaryDto>> departmentModels = departmentList.stream()
                .map(dept -> EntityModel.of(dept,
                        linkTo(methodOn(DepartmentController.class).findById(dept.id())).withSelfRel()))
                .toList();

        var selfLink = linkTo(methodOn(DepartmentController.class).findAllByInstitution(institutionId)).withSelfRel();

        CollectionModel<EntityModel<DepartmentSummaryDto>> collectionModel = CollectionModel.of(departmentModels, selfLink);

        return ResponseEntity.ok(collectionModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDetailsDto> update(@PathVariable Long id, @RequestBody @Valid UpdateDepartmentRequestDto request) {
        DepartmentDetailsDto updatedDepartment = departmentService.update(id, request);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
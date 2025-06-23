package com.institution.management.academic_api.application.controller.institution;

import com.institution.management.academic_api.application.controller.academic.AcademicTermController;
import com.institution.management.academic_api.application.controller.academic.DepartmentController;
import com.institution.management.academic_api.application.controller.employee.EmployeeController;
import com.institution.management.academic_api.application.controller.student.StudentController;
import com.institution.management.academic_api.application.controller.teacher.TeacherController;
import com.institution.management.academic_api.application.dto.institution.CreateInstitutionRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionDetailsDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionSummaryDto;
import com.institution.management.academic_api.application.dto.institution.UpdateInstitutionRequestDto;
import com.institution.management.academic_api.domain.service.institution.InstitutionService;
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
@RequestMapping("/api/v1/institutions")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;
    private final DepartmentController departmentController;
    private final EmployeeController employeeController;
    private final StudentController studentController;
    private final TeacherController teacherController;
    private final AcademicTermController academicTermController;

    @PostMapping
    public ResponseEntity<InstitutionDetailsDto> create(@RequestBody @Valid CreateInstitutionRequestDto request) {
        InstitutionDetailsDto createdInstitution = institutionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInstitution);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<InstitutionDetailsDto>> findById(@PathVariable Long id) {
        InstitutionDetailsDto institution = institutionService.findById(id);

        EntityModel<InstitutionDetailsDto> model = EntityModel.of(institution,
                linkTo(methodOn(InstitutionController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(DepartmentController.class).findAllByInstitution(id)).withRel("departments"),
                linkTo(methodOn(EmployeeController.class).findAllByInstitution(id)).withRel("employees"),
                linkTo(methodOn(StudentController.class).findAllByInstitution(id)).withRel("students"),
                linkTo(methodOn(TeacherController.class).findAllByInstitution(id)).withRel("teachers"),
                linkTo(methodOn(AcademicTermController.class).findAllByInstitution(id)).withRel("academic-terms")
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<InstitutionSummaryDto>>> findAll() {
        List<InstitutionSummaryDto> institutions = institutionService.findAll();

        List<EntityModel<InstitutionSummaryDto>> institutionModels = institutions.stream()
                .map(inst -> EntityModel.of(inst,
                        linkTo(methodOn(InstitutionController.class).findById(inst.id())).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<InstitutionSummaryDto>> collectionModel = CollectionModel.of(institutionModels,
                linkTo(methodOn(InstitutionController.class).findAll()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstitutionDetailsDto> update(@PathVariable Long id, @RequestBody @Valid UpdateInstitutionRequestDto request) {
        InstitutionDetailsDto updatedInstitution = institutionService.update(id, request);
        return ResponseEntity.ok(updatedInstitution);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        institutionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
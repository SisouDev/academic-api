package com.institution.management.academic_api.application.controller.institution;

import com.institution.management.academic_api.application.controller.academic.AcademicTermController;
import com.institution.management.academic_api.application.controller.academic.DepartmentController;
import com.institution.management.academic_api.application.controller.course.CourseSectionController;
import com.institution.management.academic_api.application.controller.student.StudentController;
import com.institution.management.academic_api.application.controller.teacher.TeacherController;
import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import com.institution.management.academic_api.application.dto.institution.CreateInstitutionRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionDetailsDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionSummaryDto;
import com.institution.management.academic_api.application.dto.institution.UpdateInstitutionRequestDto;
import com.institution.management.academic_api.domain.service.institution.InstitutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
    private final PagedResourcesAssembler<InstitutionSummaryDto> pagedAssembler;
    private final DepartmentController departmentController;
    private final AcademicTermController academicTermController;

    @PostMapping
    public ResponseEntity<EntityModel<InstitutionDetailsDto>> create(@RequestBody @Valid CreateInstitutionRequestDto request) {
        InstitutionDetailsDto created = institutionService.create(request);
        EntityModel<InstitutionDetailsDto> model = EntityModel.of(created,
                linkTo(methodOn(InstitutionController.class).findById(created.id())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<InstitutionDetailsDto>> findById(@PathVariable Long id) {
        InstitutionDetailsDto institution = institutionService.findById(id);
        EntityModel<InstitutionDetailsDto> model = EntityModel.of(institution,
                linkTo(methodOn(InstitutionController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(DepartmentController.class).findAll(null, Pageable.unpaged())).withRel("departments"),
                linkTo(methodOn(AcademicTermController.class).findAllByInstitution(id)).withRel("academic-terms"));
        return ResponseEntity.ok(model);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<InstitutionSummaryDto>>> findAll(Pageable pageable) {
        Page<InstitutionSummaryDto> institutionPage = institutionService.findPaginated(pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(institutionPage,
                inst -> EntityModel.of(inst,
                        linkTo(methodOn(InstitutionController.class).findById(inst.id())).withSelfRel())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<InstitutionDetailsDto>> update(@PathVariable Long id, @RequestBody @Valid UpdateInstitutionRequestDto request) {
        InstitutionDetailsDto updated = institutionService.update(id, request);
        EntityModel<InstitutionDetailsDto> model = EntityModel.of(updated,
                linkTo(methodOn(InstitutionController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        institutionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/teachers")
    public ResponseEntity<CollectionModel<EntityModel<PersonSummaryDto>>> findTeachersByInstitution(@PathVariable Long id) {
        List<PersonSummaryDto> teachers = institutionService.findTeachersByInstitution(id);
        List<EntityModel<PersonSummaryDto>> teacherModels = teachers.stream()
                .map(teacher -> EntityModel.of(teacher,
                        linkTo(methodOn(TeacherController.class).findById(teacher.id())).withSelfRel()))
                .collect(Collectors.toList());

        var selfLink = linkTo(methodOn(InstitutionController.class).findTeachersByInstitution(id)).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(teacherModels, selfLink));
    }

    @GetMapping("/{id}/course-sections")
    public ResponseEntity<CollectionModel<EntityModel<CourseSectionSummaryDto>>> findCourseSectionsByInstitution(@PathVariable Long id) {
        List<CourseSectionSummaryDto> sections = institutionService.findCourseSectionsByInstitution(id);

        List<EntityModel<CourseSectionSummaryDto>> sectionModels = sections.stream()
                .map(section -> EntityModel.of(section,
                        linkTo(methodOn(CourseSectionController.class).findById(section.id())).withSelfRel()))
                .collect(Collectors.toList());

        var selfLink = linkTo(methodOn(InstitutionController.class).findCourseSectionsByInstitution(id)).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(sectionModels, selfLink));
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<CollectionModel<EntityModel<PersonSummaryDto>>> findStudentsByInstitution(@PathVariable Long id) {
        List<PersonSummaryDto> students = institutionService.findStudentsByInstitution(id);
        List<EntityModel<PersonSummaryDto>> studentModels = students.stream()
                .map(student -> EntityModel.of(student,
                        linkTo(methodOn(StudentController.class).findById(student.id())).withSelfRel()))
                .collect(Collectors.toList());

        var selfLink = linkTo(methodOn(InstitutionController.class).findStudentsByInstitution(id)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(studentModels, selfLink));
    }
}
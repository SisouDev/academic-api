package com.institution.management.academic_api.application.controller.teacher;

import com.institution.management.academic_api.application.controller.course.CourseSectionController;
import com.institution.management.academic_api.application.controller.institution.InstitutionController;
import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import com.institution.management.academic_api.application.dto.teacher.CreateTeacherRequestDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherResponseDto;
import com.institution.management.academic_api.application.dto.teacher.UpdateTeacherRequestDto;
import com.institution.management.academic_api.domain.service.teacher.TeacherService;
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
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;


    @PostMapping
    public ResponseEntity<TeacherResponseDto> create(@RequestBody @Valid CreateTeacherRequestDto request) {
        TeacherResponseDto createdTeacher = teacherService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacher);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TeacherResponseDto>> findById(@PathVariable Long id) {
        TeacherResponseDto teacher = teacherService.findById(id);

        EntityModel<TeacherResponseDto> model = EntityModel.of(teacher,
                linkTo(methodOn(TeacherController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(InstitutionController.class).findById(teacher.getInstitution().id())).withRel("institution")
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PersonSummaryDto>>> findAllByInstitution(@RequestParam Long institutionId) {
        List<PersonSummaryDto> teachers = teacherService.findAllByInstitution(institutionId);

        List<EntityModel<PersonSummaryDto>> teacherModels = teachers.stream()
                .map(teacher -> EntityModel.of(teacher,
                        linkTo(methodOn(TeacherController.class).findById(teacher.id())).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<PersonSummaryDto>> collectionModel = CollectionModel.of(teacherModels,
                linkTo(methodOn(TeacherController.class).findAllByInstitution(institutionId)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> update(@PathVariable Long id, @RequestBody @Valid UpdateTeacherRequestDto request) {
        TeacherResponseDto updatedTeacher = teacherService.update(id, request);
        return ResponseEntity.ok(updatedTeacher);
    }

    @GetMapping("/{teacherId}/course-sections")
    public ResponseEntity<CollectionModel<EntityModel<CourseSectionSummaryDto>>> findCourseSectionsByTeacher(@PathVariable Long teacherId) {
        List<CourseSectionSummaryDto> sections = teacherService.findCourseSectionsByTeacherId(teacherId);

        List<EntityModel<CourseSectionSummaryDto>> sectionModels = sections.stream()
                .map(section -> EntityModel.of(section,
                        linkTo(methodOn(CourseSectionController.class).findById(section.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(sectionModels));
    }
}
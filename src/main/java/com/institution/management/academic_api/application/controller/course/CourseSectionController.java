package com.institution.management.academic_api.application.controller.course;

import com.institution.management.academic_api.application.controller.academic.AcademicTermController;
import com.institution.management.academic_api.application.controller.academic.LessonController;
import com.institution.management.academic_api.application.dto.course.CourseSectionDetailsDto;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import com.institution.management.academic_api.application.dto.course.CreateCourseSectionRequestDto;
import com.institution.management.academic_api.application.dto.course.UpdateCourseSectionRequestDto;
import com.institution.management.academic_api.application.dto.teacher.CourseSectionDetailsForTeacherDto;
import com.institution.management.academic_api.domain.service.course.CourseSectionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/course-sections")
@RequiredArgsConstructor
public class CourseSectionController {

    private final CourseSectionService courseSectionService;

    @PostMapping
    public ResponseEntity<CourseSectionDetailsDto> create(@RequestBody @Valid CreateCourseSectionRequestDto request) {
        CourseSectionDetailsDto createdSection = courseSectionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSection);
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<EntityModel<CourseSectionDetailsDto>> findById(@PathVariable Long id) {
        CourseSectionDetailsDto section = courseSectionService.findById(id);

        EntityModel<CourseSectionDetailsDto> sectionModel = EntityModel.of(section,
                linkTo(methodOn(CourseSectionController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(AcademicTermController.class).findById(section.academicTerm().id())).withRel("academic-term"),
                linkTo(methodOn(SubjectController.class).findById(section.subject().id())).withRel("subject")
        );

        return ResponseEntity.ok(sectionModel);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CourseSectionSummaryDto>>> findAllByAcademicTerm(@RequestParam Long academicTermId) {
        List<CourseSectionSummaryDto> sections = courseSectionService.findAllByAcademicTerm(academicTermId);

        List<EntityModel<CourseSectionSummaryDto>> sectionModels = sections.stream()
                .map(sec -> EntityModel.of(sec,
                        linkTo(methodOn(CourseSectionController.class).findById(sec.id())).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<CourseSectionSummaryDto>> collectionModel = CollectionModel.of(sectionModels,
                linkTo(methodOn(CourseSectionController.class).findAllByAcademicTerm(academicTermId)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}/details-for-teacher")
    @Operation(summary = "Busca os detalhes e resumos de uma turma para o professor")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<EntityModel<CourseSectionDetailsForTeacherDto>> getDetailsForTeacher(@PathVariable Long id) {
        CourseSectionDetailsForTeacherDto details = courseSectionService.findDetailsForTeacher(id);

        EntityModel<CourseSectionDetailsForTeacherDto> model = EntityModel.of(details,
                linkTo(methodOn(CourseSectionController.class).getDetailsForTeacher(id)).withSelfRel(),
                linkTo(methodOn(SubjectController.class).findById(details.subjectId())).withRel("subject"),
                linkTo(methodOn(LessonController.class).findAllBySection(id)).withRel("lessons")
        );

        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseSectionDetailsDto> update(@PathVariable Long id, @RequestBody @Valid UpdateCourseSectionRequestDto request) {
        CourseSectionDetailsDto updatedSection = courseSectionService.update(id, request);
        return ResponseEntity.ok(updatedSection);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseSectionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
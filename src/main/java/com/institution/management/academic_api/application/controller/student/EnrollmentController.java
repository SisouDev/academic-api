package com.institution.management.academic_api.application.controller.student;

import com.institution.management.academic_api.application.controller.course.CourseSectionController;
import com.institution.management.academic_api.application.dto.student.*;
import com.institution.management.academic_api.domain.service.student.EnrollmentService;
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
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<EnrollmentDetailsDto> enrollStudent(@RequestBody @Valid CreateEnrollmentRequestDto request) {
        EnrollmentDetailsDto newEnrollment = enrollmentService.enrollStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEnrollment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EnrollmentDetailsDto>> findById(@PathVariable Long id) {
        EnrollmentDetailsDto enrollment = enrollmentService.findById(id);

        EntityModel<EnrollmentDetailsDto> model = EntityModel.of(enrollment,
                linkTo(methodOn(EnrollmentController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(StudentController.class).findById(enrollment.student().id())).withRel("student"),
                linkTo(methodOn(CourseSectionController.class).findById(enrollment.courseSection().id())).withRel("course-section"),
                linkTo(methodOn(AssessmentController.class).findAssessmentsByEnrollment(id)).withRel("assessments")
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<EnrollmentSummaryDto>>> findEnrollmentsByStudent(@RequestParam Long studentId) {
        List<EnrollmentSummaryDto> enrollments = enrollmentService.findEnrollmentsByStudent(studentId);

        List<EntityModel<EnrollmentSummaryDto>> enrollmentModels = enrollments.stream()
                .map(enr -> EntityModel.of(enr,
                        linkTo(methodOn(EnrollmentController.class).findById(enr.id())).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<EnrollmentSummaryDto>> collectionModel = CollectionModel.of(enrollmentModels,
                linkTo(methodOn(EnrollmentController.class).findEnrollmentsByStudent(studentId)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateEnrollmentStatus(@PathVariable Long id, @RequestBody @Valid UpdateEnrollmentRequestDto request) {
        enrollmentService.updateEnrollmentStatus(id, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/attendance")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Registra a presença ou falta de um aluno")
    public ResponseEntity<Void> recordAttendance(@RequestBody @Valid CreateAttendanceRecordRequestDto request) {
        enrollmentService.recordAttendance(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/section/{sectionId}/class-list")
    @Operation(summary = "Busca a lista de alunos de uma turma para o registro de frequência")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CollectionModel<EntityModel<ClassListStudentDto>>> getClassListForAttendance(@PathVariable Long sectionId) {
        List<ClassListStudentDto> classList = enrollmentService.findEnrollmentsByCourseSection(sectionId);

        List<EntityModel<ClassListStudentDto>> studentModels = classList.stream()
                .map(student -> EntityModel.of(student,
                        linkTo(methodOn(StudentController.class).findById(student.studentId())).withRel("studentProfile")
                )).collect(Collectors.toList());

        var selfLink = linkTo(methodOn(EnrollmentController.class).getClassListForAttendance(sectionId)).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(studentModels, selfLink));
    }

    @GetMapping("/by-section/{courseSectionId}")
    public ResponseEntity<CollectionModel<EntityModel<ClassListStudentDto>>> getEnrollmentsBySection(@PathVariable Long courseSectionId) {
        List<ClassListStudentDto> enrollments = enrollmentService.findEnrollmentsByCourseSection(courseSectionId);

        List<EntityModel<ClassListStudentDto>> enrollmentModels = enrollments.stream()
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(EnrollmentController.class).findById(dto.enrollmentId())).withSelfRel()))
                .collect(Collectors.toList());

        var selfLink = linkTo(methodOn(EnrollmentController.class).getEnrollmentsBySection(courseSectionId)).withSelfRel();

        CollectionModel<EntityModel<ClassListStudentDto>> collectionModel = CollectionModel.of(enrollmentModels, selfLink);

        return ResponseEntity.ok(collectionModel);
    }
}

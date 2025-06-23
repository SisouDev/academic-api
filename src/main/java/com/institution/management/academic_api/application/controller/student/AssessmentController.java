package com.institution.management.academic_api.application.controller.student;

import com.institution.management.academic_api.application.dto.student.AssessmentDto;
import com.institution.management.academic_api.application.dto.student.CreateAssessmentRequestDto;
import com.institution.management.academic_api.application.dto.student.UpdateAssessmentRequestDto;
import com.institution.management.academic_api.domain.service.student.AssessmentService;
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
@RequestMapping("/api/v1/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final EnrollmentController enrollmentController;

    @PostMapping
    public ResponseEntity<AssessmentDto> addAssessmentToEnrollment(@RequestBody @Valid CreateAssessmentRequestDto request) {
        AssessmentDto newAssessment = assessmentService.addAssessmentToEnrollment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAssessment);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<AssessmentDto>>> findAssessmentsByEnrollment(@RequestParam Long enrollmentId) {
        List<AssessmentDto> assessments = assessmentService.findAssessmentsByEnrollment(enrollmentId);

        List<EntityModel<AssessmentDto>> assessmentModels = assessments.stream()
                .map(assessment -> EntityModel.of(assessment,
                        linkTo(methodOn(AssessmentController.class).updateAssessment(assessment.id(), null)).withRel("update"),
                        linkTo(methodOn(AssessmentController.class).deleteAssessment(assessment.id())).withRel("delete")
                ))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<AssessmentDto>> collectionModel = CollectionModel.of(assessmentModels,
                linkTo(methodOn(AssessmentController.class).findAssessmentsByEnrollment(enrollmentId)).withSelfRel(),
                linkTo(methodOn(EnrollmentController.class).findById(enrollmentId)).withRel("enrollment")
        );

        return ResponseEntity.ok(collectionModel);
    }

    @PutMapping("/{assessmentId}")
    public ResponseEntity<AssessmentDto> updateAssessment(@PathVariable Long assessmentId, @RequestBody @Valid UpdateAssessmentRequestDto request) {
        AssessmentDto updatedAssessment = assessmentService.updateAssessment(assessmentId, request);
        return ResponseEntity.ok(updatedAssessment);
    }

    @DeleteMapping("/{assessmentId}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long assessmentId) {
        assessmentService.deleteAssessment(assessmentId);
        return ResponseEntity.noContent().build();
    }
}
package com.institution.management.academic_api.application.controller.student;

import com.institution.management.academic_api.application.dto.student.AssessmentDto;
import com.institution.management.academic_api.application.dto.student.CreateAssessmentRequestDto;
import com.institution.management.academic_api.application.dto.student.UpdateAssessmentRequestDto;
import com.institution.management.academic_api.domain.service.student.AssessmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    @PostMapping
    public ResponseEntity<AssessmentDto> addAssessmentToEnrollment(@RequestBody @Valid CreateAssessmentRequestDto request) {
        AssessmentDto newAssessment = assessmentService.addAssessmentToEnrollment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAssessment);
    }

    @GetMapping
    public ResponseEntity<List<AssessmentDto>> findAssessmentsByEnrollment(@RequestParam Long enrollmentId) {
        List<AssessmentDto> assessments = assessmentService.findAssessmentsByEnrollment(enrollmentId);
        return ResponseEntity.ok(assessments);
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
package com.institution.management.academic_api.application.controller.student;

import com.institution.management.academic_api.application.dto.student.*;
import com.institution.management.academic_api.domain.service.student.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<EnrollmentDetailsDto> findById(@PathVariable Long id) {
        EnrollmentDetailsDto enrollment = enrollmentService.findById(id);
        return ResponseEntity.ok(enrollment);
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentSummaryDto>> findEnrollmentsByStudent(@RequestParam Long studentId) {
        List<EnrollmentSummaryDto> enrollments = enrollmentService.findEnrollmentsByStudent(studentId);
        return ResponseEntity.ok(enrollments);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateEnrollmentStatus(@PathVariable Long id, @RequestBody @Valid UpdateEnrollmentRequestDto request) {
        enrollmentService.updateEnrollmentStatus(id, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/attendance")
    public ResponseEntity<Void> recordAttendance(@RequestBody @Valid CreateAttendanceRecordRequestDto request) {
        enrollmentService.recordAttendance(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
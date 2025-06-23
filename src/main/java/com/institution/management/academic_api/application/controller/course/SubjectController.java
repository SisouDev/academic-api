package com.institution.management.academic_api.application.controller.course;

import com.institution.management.academic_api.application.dto.course.CreateSubjectRequestDto;
import com.institution.management.academic_api.application.dto.course.SubjectDetailsDto;
import com.institution.management.academic_api.application.dto.course.SubjectSummaryDto;
import com.institution.management.academic_api.application.dto.course.UpdateSubjectRequestDto;
import com.institution.management.academic_api.domain.service.course.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SubjectDetailsDto> create(@RequestBody @Valid CreateSubjectRequestDto request) {
        SubjectDetailsDto createdSubject = subjectService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDetailsDto> findById(@PathVariable Long id) {
        SubjectDetailsDto subject = subjectService.findById(id);
        return ResponseEntity.ok(subject);
    }

    @GetMapping
    public ResponseEntity<List<SubjectSummaryDto>> findAllByCourse(@RequestParam Long courseId) {
        List<SubjectSummaryDto> subjects = subjectService.findAllByCourse(courseId);
        return ResponseEntity.ok(subjects);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDetailsDto> update(@PathVariable Long id, @RequestBody @Valid UpdateSubjectRequestDto request) {
        SubjectDetailsDto updatedSubject = subjectService.update(id, request);
        return ResponseEntity.ok(updatedSubject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
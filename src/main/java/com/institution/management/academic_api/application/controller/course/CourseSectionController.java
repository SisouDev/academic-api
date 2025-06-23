package com.institution.management.academic_api.application.controller.course;

import com.institution.management.academic_api.application.dto.course.CourseSectionDetailsDto;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import com.institution.management.academic_api.application.dto.course.CreateCourseSectionRequestDto;
import com.institution.management.academic_api.application.dto.course.UpdateCourseSectionRequestDto;
import com.institution.management.academic_api.domain.service.course.CourseSectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<CourseSectionDetailsDto> findById(@PathVariable Long id) {
        CourseSectionDetailsDto section = courseSectionService.findById(id);
        return ResponseEntity.ok(section);
    }

    @GetMapping
    public ResponseEntity<List<CourseSectionSummaryDto>> findAllByAcademicTerm(@RequestParam Long academicTermId) {
        List<CourseSectionSummaryDto> sections = courseSectionService.findAllByAcademicTerm(academicTermId);
        return ResponseEntity.ok(sections);
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
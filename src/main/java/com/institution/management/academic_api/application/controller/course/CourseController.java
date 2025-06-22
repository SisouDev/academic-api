package com.institution.management.academic_api.application.controller.course;

import com.institution.management.academic_api.application.dto.course.CourseDetailsDto;
import com.institution.management.academic_api.application.dto.course.CourseSummaryDto;
import com.institution.management.academic_api.application.dto.course.CreateCourseRequestDto;
import com.institution.management.academic_api.application.dto.course.UpdateCourseRequestDto;
import com.institution.management.academic_api.domain.service.course.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDetailsDto> create(@RequestBody @Valid CreateCourseRequestDto request) {
        CourseDetailsDto createdCourse = courseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailsDto> findById(@PathVariable Long id) {
        CourseDetailsDto course = courseService.findById(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping
    public ResponseEntity<List<CourseSummaryDto>> findAllByDepartment(@RequestParam Long departmentId) {
        List<CourseSummaryDto> courses = courseService.findAllByDepartment(departmentId);
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDetailsDto> update(@PathVariable Long id, @RequestBody @Valid UpdateCourseRequestDto request) {
        CourseDetailsDto updatedCourse = courseService.update(id, request);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
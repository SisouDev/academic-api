package com.institution.management.academic_api.application.controller.course;

import com.institution.management.academic_api.application.dto.course.CourseDetailsDto;
import com.institution.management.academic_api.application.dto.course.CourseSummaryDto;
import com.institution.management.academic_api.application.dto.course.CreateCourseRequestDto;
import com.institution.management.academic_api.application.dto.course.UpdateCourseRequestDto;
import com.institution.management.academic_api.domain.service.course.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<EntityModel<CourseDetailsDto>> findById(@PathVariable Long id) {
        CourseDetailsDto course = courseService.findById(id);

        EntityModel<CourseDetailsDto> courseModel = EntityModel.of(course,
                linkTo(methodOn(CourseController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(SubjectController.class).findAllByCourse(id)).withRel("subjectsSummary")
        );

        return ResponseEntity.ok(courseModel);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<CourseSummaryDto>>> findPaginated(
            @RequestParam(required = false) String searchTerm,
            Pageable pageable,
            PagedResourcesAssembler<CourseSummaryDto> assembler) {

        Page<CourseSummaryDto> coursesPage = courseService.findPaginated(searchTerm, pageable);

        return ResponseEntity.ok(assembler.toModel(coursesPage,
                course -> EntityModel.of(course,
                        linkTo(methodOn(CourseController.class).findById(course.id())).withSelfRel())
        ));
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
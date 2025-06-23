package com.institution.management.academic_api.application.controller.teacher;

import com.institution.management.academic_api.application.controller.course.CourseSectionController;
import com.institution.management.academic_api.application.dto.teacher.CreateLessonPlanRequestDto;
import com.institution.management.academic_api.application.dto.teacher.LessonPlanDto;
import com.institution.management.academic_api.application.dto.teacher.UpdateLessonPlanRequestDto;
import com.institution.management.academic_api.domain.service.teacher.LessonPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LessonPlanController {

    private final LessonPlanService lessonPlanService;
    private final CourseSectionController courseSectionController;

    @PostMapping("/lesson-plans")
    public ResponseEntity<LessonPlanDto> create(@RequestBody @Valid CreateLessonPlanRequestDto request) {
        LessonPlanDto createdLessonPlan = lessonPlanService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLessonPlan);
    }

    @GetMapping("/course-sections/{courseSectionId}/lesson-plan")
    public ResponseEntity<EntityModel<LessonPlanDto>> findByCourseSection(@PathVariable Long courseSectionId) {
        LessonPlanDto lessonPlan = lessonPlanService.findByCourseSection(courseSectionId);

        EntityModel<LessonPlanDto> model = EntityModel.of(lessonPlan,
                linkTo(methodOn(LessonPlanController.class).findByCourseSection(courseSectionId)).withSelfRel(),
                linkTo(methodOn(CourseSectionController.class).findById(courseSectionId)).withRel("course-section")
        );

        return ResponseEntity.ok(model);
    }

    @PutMapping("/lesson-plans/{lessonPlanId}")
    public ResponseEntity<LessonPlanDto> update(@PathVariable Long lessonPlanId, @RequestBody @Valid UpdateLessonPlanRequestDto request) {
        LessonPlanDto updatedLessonPlan = lessonPlanService.update(lessonPlanId, request);
        return ResponseEntity.ok(updatedLessonPlan);
    }

    @DeleteMapping("/lesson-plans/{lessonPlanId}")
    public ResponseEntity<Void> delete(@PathVariable Long lessonPlanId) {
        lessonPlanService.delete(lessonPlanId);
        return ResponseEntity.noContent().build();
    }
}
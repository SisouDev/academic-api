package com.institution.management.academic_api.application.controller.course;

import com.institution.management.academic_api.application.dto.course.CreateSubjectRequestDto;
import com.institution.management.academic_api.application.dto.course.SubjectDetailsDto;
import com.institution.management.academic_api.application.dto.course.SubjectSummaryDto;
import com.institution.management.academic_api.application.dto.course.UpdateSubjectRequestDto;
import com.institution.management.academic_api.application.dto.student.StudentSubjectDetailsDto;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.service.course.SubjectService;
import com.institution.management.academic_api.domain.service.student.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<SubjectDetailsDto> create(@RequestBody @Valid CreateSubjectRequestDto request) {
        SubjectDetailsDto createdSubject = subjectService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SubjectDetailsDto>> findById(@PathVariable Long id) {
        SubjectDetailsDto subject = subjectService.findById(id);

        EntityModel<SubjectDetailsDto> subjectModel = EntityModel.of(subject,
                linkTo(methodOn(SubjectController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(CourseController.class).findById(subject.course().id())).withRel("course")
        );

        return ResponseEntity.ok(subjectModel);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<SubjectSummaryDto>>> findAllByCourse(@RequestParam Long courseId) {
        List<SubjectSummaryDto> subjects = subjectService.findAllByCourse(courseId);

        List<EntityModel<SubjectSummaryDto>> subjectModels = subjects.stream()
                .map(sub -> EntityModel.of(sub,
                        linkTo(methodOn(SubjectController.class).findById(sub.id())).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<SubjectSummaryDto>> collectionModel = CollectionModel.of(subjectModels,
                linkTo(methodOn(SubjectController.class).findAllByCourse(courseId)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{subjectId}/details")
    @Operation(summary = "Busca os detalhes de uma matéria na qual o aluno está matriculado")
    public ResponseEntity<StudentSubjectDetailsDto> getSubjectDetailsForStudent(
            @PathVariable Long subjectId,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        StudentSubjectDetailsDto details = subjectService.findSubjectDetailsForPage(subjectId);

        return ResponseEntity.ok(details);
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
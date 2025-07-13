package com.institution.management.academic_api.application.controller.academic;

import com.institution.management.academic_api.application.dto.academic.CreateLessonRequestDto;
import com.institution.management.academic_api.application.dto.academic.LessonDetailsDto;
import com.institution.management.academic_api.application.dto.academic.LessonSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateLessonRequestDto;
import com.institution.management.academic_api.domain.service.academic.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
@Tag(name = "Aulas", description = "Endpoints para gerenciamento de aulas")
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Cria uma nova aula para uma turma")
    public ResponseEntity<EntityModel<LessonDetailsDto>> create(@RequestBody @Valid CreateLessonRequestDto request) {
        LessonDetailsDto createdLesson = lessonService.create(request);
        return new ResponseEntity<>(addLinks(createdLesson), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca os detalhes de uma aula pelo ID")
    public ResponseEntity<EntityModel<LessonDetailsDto>> findById(@PathVariable Long id) {
        LessonDetailsDto lesson = lessonService.findById(id);
        return ResponseEntity.ok(addLinks(lesson));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Atualiza uma aula existente")
    public ResponseEntity<EntityModel<LessonDetailsDto>> update(@PathVariable Long id, @RequestBody @Valid UpdateLessonRequestDto request) {
        LessonDetailsDto updatedLesson = lessonService.update(id, request);
        return ResponseEntity.ok(addLinks(updatedLesson));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Deleta uma aula")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lessonService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/section/{sectionId}")
    @Operation(summary = "Busca todas as aulas de uma turma específica")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CollectionModel<EntityModel<LessonSummaryDto>>> findAllBySection(@PathVariable Long sectionId) {
        List<LessonSummaryDto> lessons = lessonService.findAllBySection(sectionId);

        List<EntityModel<LessonSummaryDto>> lessonModels = lessons.stream()
                .map(lesson -> EntityModel.of(lesson,
                        linkTo(methodOn(LessonController.class).findById(lesson.id())).withSelfRel()))
                .collect(Collectors.toList());

        var selfLink = linkTo(methodOn(LessonController.class).findAllBySection(sectionId)).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(lessonModels, selfLink));
    }

    private EntityModel<LessonDetailsDto> addLinks(LessonDetailsDto lesson) {
        return EntityModel.of(lesson,
                linkTo(methodOn(LessonController.class).findById(lesson.id())).withSelfRel());
    }
}
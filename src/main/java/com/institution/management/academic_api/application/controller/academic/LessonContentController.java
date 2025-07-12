package com.institution.management.academic_api.application.controller.academic;

import com.institution.management.academic_api.application.dto.academic.CreateLessonContentRequestDto;
import com.institution.management.academic_api.application.dto.academic.LessonContentDto;
import com.institution.management.academic_api.application.dto.academic.UpdateLessonContentRequestDto;
import com.institution.management.academic_api.domain.service.academic.LessonContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lesson-contents")
@RequiredArgsConstructor
@Tag(name = "Conteúdo de Aulas", description = "Endpoints para gerenciamento de conteúdos de uma aula")
public class LessonContentController {

    private final LessonContentService lessonContentService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Adiciona um novo bloco de conteúdo a uma aula")
    public ResponseEntity<EntityModel<LessonContentDto>> create(@RequestBody @Valid CreateLessonContentRequestDto request) {
        LessonContentDto createdContent = lessonContentService.create(request);
        return new ResponseEntity<>(addLinks(createdContent), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Atualiza um bloco de conteúdo")
    public ResponseEntity<EntityModel<LessonContentDto>> update(@PathVariable Long id, @RequestBody @Valid UpdateLessonContentRequestDto request) {
        LessonContentDto updatedContent = lessonContentService.update(id, request);
        return ResponseEntity.ok(addLinks(updatedContent));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Deleta um bloco de conteúdo")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lessonContentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<LessonContentDto> addLinks(LessonContentDto content) {
        return EntityModel.of(content);
    }
}
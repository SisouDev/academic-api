package com.institution.management.academic_api.application.controller.teacher;

import com.institution.management.academic_api.application.controller.student.EnrollmentController;
import com.institution.management.academic_api.application.dto.teacher.CreateTeacherNoteRequestDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherNoteDto;
import com.institution.management.academic_api.domain.service.teacher.TeacherNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/teacher-notes")
@RequiredArgsConstructor
public class TeacherNoteController {

    private final TeacherNoteService noteService;

    @PostMapping
    public ResponseEntity<EntityModel<TeacherNoteDto>> createNote(@RequestBody @Valid CreateTeacherNoteRequestDto request) {
        TeacherNoteDto createdNote = noteService.create(request);

        EntityModel<TeacherNoteDto> noteModel = EntityModel.of(createdNote,
                linkTo(methodOn(TeacherNoteController.class).findById(createdNote.id())).withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(noteModel);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<EntityModel<TeacherNoteDto>> findById(@PathVariable Long noteId) {
        TeacherNoteDto note = noteService.findById(noteId);

        EntityModel<TeacherNoteDto> noteModel = EntityModel.of(note,
                linkTo(methodOn(TeacherNoteController.class).findById(noteId)).withSelfRel(),
                linkTo(methodOn(EnrollmentController.class).findById(note.enrollmentId())).withRel("enrollment")
        );

        return ResponseEntity.ok(noteModel);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<TeacherNoteDto>>> getNotesByEnrollment(@RequestParam Long enrollmentId) {
        List<TeacherNoteDto> notes = noteService.findByEnrollment(enrollmentId);

        List<EntityModel<TeacherNoteDto>> noteModels = notes.stream()
                .map(note -> EntityModel.of(note,
                        linkTo(methodOn(TeacherNoteController.class).findById(note.id())).withSelfRel()))
                .collect(Collectors.toList());

        var selfLink = linkTo(methodOn(TeacherNoteController.class).getNotesByEnrollment(enrollmentId)).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(noteModels, selfLink));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId) {
        noteService.delete(noteId);
        return ResponseEntity.noContent().build();
    }
}
package com.institution.management.academic_api.application.controller.teacher;

import com.institution.management.academic_api.application.controller.course.CourseSectionController;
import com.institution.management.academic_api.application.controller.institution.InstitutionController;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import com.institution.management.academic_api.application.dto.teacher.*;
import com.institution.management.academic_api.domain.service.teacher.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;


    @PostMapping
    public ResponseEntity<TeacherResponseDto> create(@RequestBody @Valid CreateTeacherRequestDto request) {
        TeacherResponseDto createdTeacher = teacherService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacher);
    }

    @GetMapping("/me/sections")
    @Operation(summary = "Busca um resumo HATEOAS de todas as turmas ativas do professor logado")
    public ResponseEntity<CollectionModel<EntityModel<TeacherCourseSectionDto>>> getMySections() {
        List<TeacherCourseSectionDto> sections = teacherService.findSectionsForCurrentTeacher();

        List<EntityModel<TeacherCourseSectionDto>> sectionModels = sections.stream()
                .map(section -> EntityModel.of(section,
                        linkTo(methodOn(CourseSectionController.class).findById(section.id())).withSelfRel()
                )).collect(Collectors.toList());
        var selfLink = linkTo(methodOn(TeacherController.class).getMySections()).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(sectionModels, selfLink));
    }

    @GetMapping("/{teacherId}/course-sections")
    public ResponseEntity<CollectionModel<EntityModel<CourseSectionSummaryDto>>> findCourseSectionsByTeacher(@PathVariable Long teacherId) {
        List<CourseSectionSummaryDto> sections = teacherService.findCourseSectionsByTeacherId(teacherId);

        List<EntityModel<CourseSectionSummaryDto>> sectionModels = sections.stream()
                .map(section -> EntityModel.of(section,
                        linkTo(methodOn(CourseSectionController.class).findById(section.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(sectionModels));
    }

    @GetMapping("/me/students")
    @Operation(summary = "Busca uma lista consolidada de todos os alunos de um professor")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CollectionModel<EntityModel<TeacherStudentListDto>>> getAllMyStudents() {
        List<TeacherStudentListDto> students = teacherService.findAllStudentsForCurrentTeacher();

        List<EntityModel<TeacherStudentListDto>> studentModels = students.stream()
                .map(student -> EntityModel.of(student,
                        linkTo(methodOn(TeacherNoteController.class).getNotesByEnrollment(student.enrollmentId())).withRel("notes")
                        //linkTo(methodOn(StudentController.class).findById(student.studentId())).withRel("studentProfile")
                )).collect(Collectors.toList());

        var selfLink = linkTo(methodOn(TeacherController.class).getAllMyStudents()).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(studentModels, selfLink));
    }

    @GetMapping("/my-sections")
    @Operation(summary = "Busca um resumo das turmas do professor logado para o dashboard de aulas")
    public ResponseEntity<List<TeacherCourseSectionDto>> getMySectionsForDashboard() {
        List<TeacherCourseSectionDto> sections = teacherService.findSectionsForCurrentTeacherDashboard();
        return ResponseEntity.ok(sections);
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<EntityModel<TeacherResponseDto>> findById(@PathVariable Long id) {
        TeacherResponseDto teacher = teacherService.findById(id);

        EntityModel<TeacherResponseDto> model = EntityModel.of(teacher,
                linkTo(methodOn(TeacherController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(InstitutionController.class).findById(teacher.getInstitution().id())).withRel("institution")
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<TeacherSummaryDto>>> findPaginated(
            @RequestParam(required = false) String searchTerm,
            Pageable pageable,
            PagedResourcesAssembler<TeacherSummaryDto> assembler) {

        Page<TeacherSummaryDto> teachersPage = teacherService.findPaginated(searchTerm, pageable);

        PagedModel<EntityModel<TeacherSummaryDto>> pagedModel = assembler.toModel(teachersPage,
                teacher -> EntityModel.of(teacher,
                        linkTo(methodOn(TeacherController.class).findById(teacher.id())).withSelfRel()
                )
        );

        return ResponseEntity.ok(pagedModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> update(@PathVariable Long id, @RequestBody @Valid UpdateTeacherRequestDto request) {
        TeacherResponseDto updatedTeacher = teacherService.update(id, request);
        return ResponseEntity.ok(updatedTeacher);
    }

}
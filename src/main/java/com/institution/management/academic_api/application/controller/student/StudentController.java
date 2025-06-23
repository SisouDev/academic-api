package com.institution.management.academic_api.application.controller.student;

import com.institution.management.academic_api.application.controller.institution.InstitutionController;
import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.student.CreateStudentRequestDto;
import com.institution.management.academic_api.application.dto.student.StudentResponseDto;
import com.institution.management.academic_api.application.dto.student.UpdateStudentRequestDto;
import com.institution.management.academic_api.domain.service.student.StudentService;
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
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponseDto> create(@RequestBody @Valid CreateStudentRequestDto request) {
        StudentResponseDto createdStudent = studentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<StudentResponseDto>> findById(@PathVariable Long id) {
        StudentResponseDto student = studentService.findById(id);

        EntityModel<StudentResponseDto> model = EntityModel.of(student,
                linkTo(methodOn(StudentController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(EnrollmentController.class).findEnrollmentsByStudent(id)).withRel("enrollments"),
                linkTo(methodOn(InstitutionController.class).findById(student.getInstitution().id())).withRel("institution")
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PersonSummaryDto>>> findAllByInstitution(@RequestParam Long institutionId) {
        List<PersonSummaryDto> students = studentService.findAllByInstitution(institutionId);

        List<EntityModel<PersonSummaryDto>> studentModels = students.stream()
                .map(student -> EntityModel.of(student,
                        linkTo(methodOn(StudentController.class).findById(student.id())).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<PersonSummaryDto>> collectionModel = CollectionModel.of(studentModels,
                linkTo(methodOn(StudentController.class).findAllByInstitution(institutionId)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> update(@PathVariable Long id, @RequestBody @Valid UpdateStudentRequestDto request) {
        StudentResponseDto updatedStudent = studentService.update(id, request);
        return ResponseEntity.ok(updatedStudent);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        studentService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}
package com.institution.management.academic_api.application.controller.student;

import com.institution.management.academic_api.application.controller.institution.InstitutionController;
import com.institution.management.academic_api.application.dto.student.CreateStudentRequestDto;
import com.institution.management.academic_api.application.dto.student.StudentResponseDto;
import com.institution.management.academic_api.application.dto.student.StudentSummaryDto;
import com.institution.management.academic_api.application.dto.student.UpdateStudentRequestDto;
import com.institution.management.academic_api.domain.service.student.StudentService;
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

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> update(@PathVariable Long id, @RequestBody @Valid UpdateStudentRequestDto request) {
        StudentResponseDto updatedStudent = studentService.update(id, request);
        return ResponseEntity.ok(updatedStudent);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<StudentResponseDto> updateStatus(@PathVariable Long id, @RequestParam String status) {
        StudentResponseDto updatedStudent = studentService.updateStatus(id, status);
        return ResponseEntity.ok(updatedStudent);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<StudentSummaryDto>>> findPaginated(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Long institutionId,
            Pageable pageable,
            PagedResourcesAssembler<StudentSummaryDto> assembler) {

        Page<StudentSummaryDto> studentsPage = studentService.findPaginated(searchTerm, institutionId, pageable);

        PagedModel<EntityModel<StudentSummaryDto>> pagedModel = assembler.toModel(studentsPage,
                student -> EntityModel.of(student,
                        linkTo(methodOn(StudentController.class).findById(student.id())).withSelfRel()
                )
        );

        return ResponseEntity.ok(pagedModel);
    }

}
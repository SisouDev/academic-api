package com.institution.management.academic_api.application.controller.teacher;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.teacher.CreateTeacherRequestDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherResponseDto;
import com.institution.management.academic_api.application.dto.teacher.UpdateTeacherRequestDto;
import com.institution.management.academic_api.domain.service.teacher.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> findById(@PathVariable Long id) {
        TeacherResponseDto teacher = teacherService.findById(id);
        return ResponseEntity.ok(teacher);
    }

    @GetMapping
    public ResponseEntity<List<PersonSummaryDto>> findAllByInstitution(@RequestParam Long institutionId) {
        List<PersonSummaryDto> teachers = teacherService.findAllByInstitution(institutionId);
        return ResponseEntity.ok(teachers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> update(@PathVariable Long id, @RequestBody @Valid UpdateTeacherRequestDto request) {
        TeacherResponseDto updatedTeacher = teacherService.update(id, request);
        return ResponseEntity.ok(updatedTeacher);
    }
}
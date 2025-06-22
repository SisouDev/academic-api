package com.institution.management.academic_api.application.controller.academic;

import com.institution.management.academic_api.application.dto.academic.DepartmentDetailsDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentRequestDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateDepartmentRequestDto;
import com.institution.management.academic_api.domain.service.academic.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentDetailsDto> create(@RequestBody @Valid DepartmentRequestDto request) {
        DepartmentDetailsDto createdDepartment = departmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDetailsDto> findById(@PathVariable Long id) {
        DepartmentDetailsDto department = departmentService.findById(id);
        return ResponseEntity.ok(department);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentSummaryDto>> findAllByInstitution(@RequestParam Long institutionId) {
        List<DepartmentSummaryDto> departments = departmentService.findAllByInstitution(institutionId);
        return ResponseEntity.ok(departments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDetailsDto> update(@PathVariable Long id, @RequestBody @Valid UpdateDepartmentRequestDto request) {
        DepartmentDetailsDto updatedDepartment = departmentService.update(id, request);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
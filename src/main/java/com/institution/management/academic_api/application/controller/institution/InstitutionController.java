package com.institution.management.academic_api.application.controller.institution;

import com.institution.management.academic_api.application.dto.institution.CreateInstitutionRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionDetailsDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionSummaryDto;
import com.institution.management.academic_api.application.dto.institution.UpdateInstitutionRequestDto;
import com.institution.management.academic_api.domain.service.institution.InstitutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/institutions")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;

    @PostMapping
    public ResponseEntity<InstitutionDetailsDto> create(@RequestBody @Valid CreateInstitutionRequestDto request) {
        InstitutionDetailsDto createdInstitution = institutionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInstitution);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDetailsDto> findById(@PathVariable Long id) {
        InstitutionDetailsDto institution = institutionService.findById(id);
        return ResponseEntity.ok(institution);
    }

    @GetMapping
    public ResponseEntity<List<InstitutionSummaryDto>> findAll() {
        List<InstitutionSummaryDto> institutions = institutionService.findAll();
        return ResponseEntity.ok(institutions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstitutionDetailsDto> update(@PathVariable Long id, @RequestBody @Valid UpdateInstitutionRequestDto request) {
        InstitutionDetailsDto updatedInstitution = institutionService.update(id, request);
        return ResponseEntity.ok(updatedInstitution);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        institutionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
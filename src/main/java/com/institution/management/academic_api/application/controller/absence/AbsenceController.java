package com.institution.management.academic_api.application.controller.absence;

import com.institution.management.academic_api.application.dto.absence.AbsenceDetailsDto;
import com.institution.management.academic_api.application.dto.absence.CreateAbsenceRequestDto;
import com.institution.management.academic_api.application.dto.absence.ReviewAbsenceRequestDto;
import com.institution.management.academic_api.domain.service.absence.AbsenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/absences")
@RequiredArgsConstructor
@Tag(name = "Absences", description = "Endpoints para gerenciamento de faltas e justificativas")
public class AbsenceController {

    private final AbsenceService absenceService;

    @PostMapping
    @Operation(summary = "Registra uma nova justificativa de ausência")
    public ResponseEntity<EntityModel<AbsenceDetailsDto>> create(@RequestBody CreateAbsenceRequestDto request) {
        AbsenceDetailsDto createdAbsence = absenceService.create(request);
        EntityModel<AbsenceDetailsDto> resource = EntityModel.of(createdAbsence,
                linkTo(methodOn(AbsenceController.class).findById(createdAbsence.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @PostMapping("/{id}/attachment")
    @Operation(summary = "Adiciona um anexo a uma justificativa de ausência")
    public ResponseEntity<String> addAttachment(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String fileUrl = absenceService.addAttachment(id, file);
        return ResponseEntity.ok().body(fileUrl);
    }

    @PatchMapping("/{id}/review")
    @Operation(summary = "Aprova ou rejeita uma justificativa de ausência (ação administrativa)")
    public ResponseEntity<Void> review(@PathVariable Long id, @RequestBody ReviewAbsenceRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String reviewerEmail = authentication.getName();

        absenceService.review(id, request, reviewerEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma justificativa de ausência pelo ID")
    public ResponseEntity<EntityModel<AbsenceDetailsDto>> findById(@PathVariable Long id) {
        AbsenceDetailsDto absence = absenceService.findById(id);
        EntityModel<AbsenceDetailsDto> resource = EntityModel.of(absence,
                linkTo(methodOn(AbsenceController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/my-absences")
    @Operation(summary = "Lista o histórico de justificativas do usuário logado")
    public ResponseEntity<List<EntityModel<AbsenceDetailsDto>>> findMyAbsences() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        List<AbsenceDetailsDto> absences = List.of();

        List<EntityModel<AbsenceDetailsDto>> resources = absences.stream()
                .map(absence -> EntityModel.of(absence,
                        linkTo(methodOn(AbsenceController.class).findById(absence.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }
}
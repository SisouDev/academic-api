package com.institution.management.academic_api.application.controller.humanResources;

import com.institution.management.academic_api.application.dto.employee.CreateJobHistoryRequestDto;
import com.institution.management.academic_api.application.dto.employee.JobHistoryDto;
import com.institution.management.academic_api.domain.service.humanResources.HRService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/hr")
@RequiredArgsConstructor
@Tag(name = "Human Resources", description = "Endpoints para gerenciamento de RH")
@PreAuthorize("hasAnyRole('HR_ANALYST', 'ADMIN')")
public class HRController {

    private final HRService hrService;

    @PostMapping("/job-history")
    @Operation(summary = "Registra um novo evento de carreira para uma pessoa (ex: promoção)")
    public ResponseEntity<Void> recordJobHistoryEvent(@RequestBody CreateJobHistoryRequestDto dto) {
        hrService.recordJobHistoryEvent(dto);
        return ResponseEntity.created(URI.create("")).build();
    }

    @GetMapping("/job-history")
    @Operation(summary = "Busca o histórico de cargos de uma pessoa")
    public ResponseEntity<CollectionModel<EntityModel<JobHistoryDto>>> findJobHistoryByPerson(@RequestParam Long personId) {
        List<JobHistoryDto> history = hrService.findJobHistoryByPerson(personId);

        List<EntityModel<JobHistoryDto>> resources = history.stream()
                .map(h -> EntityModel.of(h,
                        linkTo(methodOn(HRController.class).findJobHistoryById(h.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resources,
                linkTo(methodOn(HRController.class).findJobHistoryByPerson(personId)).withSelfRel()));
    }

    @GetMapping("/job-history/{id}")
    @Operation(summary = "Busca um registro de histórico de cargo pelo ID")
    public ResponseEntity<EntityModel<JobHistoryDto>> findJobHistoryById(@PathVariable Long id) {
        JobHistoryDto history = hrService.findJobHistoryById(id);
        EntityModel<JobHistoryDto> resource = EntityModel.of(history,
                linkTo(methodOn(HRController.class).findJobHistoryById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }
}
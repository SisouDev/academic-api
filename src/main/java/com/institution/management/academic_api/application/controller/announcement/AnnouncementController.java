package com.institution.management.academic_api.application.controller.announcement;

import com.institution.management.academic_api.application.dto.announcement.AnnouncementDetailsDto;
import com.institution.management.academic_api.application.dto.announcement.AnnouncementSummaryDto;
import com.institution.management.academic_api.application.dto.announcement.CreateAnnouncementRequestDto;
import com.institution.management.academic_api.application.dto.announcement.UpdateAnnouncementRequestDto;
import com.institution.management.academic_api.domain.service.announcement.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
@Tag(name = "Announcements", description = "Endpoints para gerenciamento do mural de avisos")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping
    @Operation(summary = "Cria um novo aviso")
    public ResponseEntity<EntityModel<AnnouncementDetailsDto>> create(@RequestBody CreateAnnouncementRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String creatorEmail = authentication.getName();

        AnnouncementDetailsDto createdAnnouncement = announcementService.create(request, creatorEmail);
        EntityModel<AnnouncementDetailsDto> resource = EntityModel.of(createdAnnouncement,
                linkTo(methodOn(AnnouncementController.class).findById(createdAnnouncement.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza um aviso existente")
    public ResponseEntity<EntityModel<AnnouncementDetailsDto>> update(@PathVariable Long id, @RequestBody UpdateAnnouncementRequestDto request) {
        AnnouncementDetailsDto updatedAnnouncement = announcementService.update(id, request);
        EntityModel<AnnouncementDetailsDto> resource = EntityModel.of(updatedAnnouncement,
                linkTo(methodOn(AnnouncementController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um aviso")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        announcementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca os detalhes de um aviso específico")
    public ResponseEntity<EntityModel<AnnouncementDetailsDto>> findById(@PathVariable Long id) {
        AnnouncementDetailsDto announcement = announcementService.findById(id);
        EntityModel<AnnouncementDetailsDto> resource = EntityModel.of(announcement,
                linkTo(methodOn(AnnouncementController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(AnnouncementController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Busca avisos visíveis, com filtro opcional por turma")
    public ResponseEntity<CollectionModel<EntityModel<AnnouncementSummaryDto>>> findAll(
            @RequestParam(required = false) Long courseSectionId) {

        List<AnnouncementSummaryDto> announcements;

        if (courseSectionId != null) {
            announcements = announcementService.findByCourseSection(courseSectionId);
        } else {
            announcements = announcementService.findVisibleForCurrentUser();
        }

        List<EntityModel<AnnouncementSummaryDto>> resources = announcements.stream()
                .map(announcement -> EntityModel.of(announcement,
                        linkTo(methodOn(AnnouncementController.class).findById(announcement.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resources));
    }
}
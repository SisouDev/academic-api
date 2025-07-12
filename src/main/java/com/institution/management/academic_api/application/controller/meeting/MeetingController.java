package com.institution.management.academic_api.application.controller.meeting;

import com.institution.management.academic_api.application.dto.meeting.CreateMeetingRequestDto;
import com.institution.management.academic_api.application.dto.meeting.MeetingDetailsDto;
import com.institution.management.academic_api.application.dto.meeting.UpdateMeetingRequestDto;
import com.institution.management.academic_api.domain.service.meeting.MeetingService;
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
@RequestMapping("/api/v1/meetings")
@RequiredArgsConstructor
@Tag(name = "Meetings", description = "Endpoints para gerenciamento de reuniões e agendamentos")
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping
    @Operation(summary = "Agenda uma nova reunião")
    public ResponseEntity<EntityModel<MeetingDetailsDto>> create(@RequestBody CreateMeetingRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String organizerEmail = authentication.getName();
        MeetingDetailsDto createdMeeting = meetingService.create(request, organizerEmail);
        EntityModel<MeetingDetailsDto> resource = EntityModel.of(createdMeeting,
                linkTo(methodOn(MeetingController.class).findById(createdMeeting.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza uma reunião existente")
    public ResponseEntity<EntityModel<MeetingDetailsDto>> update(@PathVariable Long id, @RequestBody UpdateMeetingRequestDto request) {
        MeetingDetailsDto updatedMeeting = meetingService.update(id, request);
        EntityModel<MeetingDetailsDto> resource = EntityModel.of(updatedMeeting,
                linkTo(methodOn(MeetingController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancela ou deleta uma reunião")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        meetingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca os detalhes de uma reunião pelo ID")
    public ResponseEntity<EntityModel<MeetingDetailsDto>> findById(@PathVariable Long id) {
        MeetingDetailsDto meeting = meetingService.findById(id);
        EntityModel<MeetingDetailsDto> resource = EntityModel.of(meeting,
                linkTo(methodOn(MeetingController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/my-meetings")
    @Operation(summary = "Busca a agenda de reuniões do usuário logado para um mês específico")
    public ResponseEntity<CollectionModel<EntityModel<MeetingDetailsDto>>> findMyMeetings(
            @RequestParam int year,
            @RequestParam int month) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        List<MeetingDetailsDto> meetings = meetingService.findMyMeetings(userEmail, year, month);

        List<EntityModel<MeetingDetailsDto>> resources = meetings.stream()
                .map(meeting -> EntityModel.of(meeting,
                        linkTo(methodOn(MeetingController.class).findById(meeting.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resources,
                linkTo(methodOn(MeetingController.class).findMyMeetings(year, month)).withSelfRel()));
    }
}
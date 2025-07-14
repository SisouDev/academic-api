package com.institution.management.academic_api.application.controller.calendar;

import com.institution.management.academic_api.application.controller.meeting.MeetingController;
import com.institution.management.academic_api.application.dto.calendar.CalendarEventDetailsDto;
import com.institution.management.academic_api.application.dto.calendar.CreateCalendarEventRequestDto;
import com.institution.management.academic_api.application.dto.calendar.UpdateCalendarEventRequestDto;
import com.institution.management.academic_api.application.dto.meeting.AgendaItemDto;
import com.institution.management.academic_api.domain.service.calendar.CalendarEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/calendar/events")
@RequiredArgsConstructor
@Tag(name = "Calendar", description = "Endpoints para gerenciamento de eventos do calendário")
public class CalendarEventController {

    private final CalendarEventService calendarEventService;

    @PostMapping
    @Operation(summary = "Cria um novo evento no calendário")
    public ResponseEntity<EntityModel<CalendarEventDetailsDto>> create(@RequestBody CreateCalendarEventRequestDto request) {
        CalendarEventDetailsDto createdEvent = calendarEventService.create(request);
        EntityModel<CalendarEventDetailsDto> resource = EntityModel.of(createdEvent,
                linkTo(methodOn(CalendarEventController.class).findById(createdEvent.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza um evento existente")
    public ResponseEntity<EntityModel<CalendarEventDetailsDto>> update(@PathVariable Long id, @RequestBody UpdateCalendarEventRequestDto request) {
        CalendarEventDetailsDto updatedEvent = calendarEventService.update(id, request);
        EntityModel<CalendarEventDetailsDto> resource = EntityModel.of(updatedEvent,
                linkTo(methodOn(CalendarEventController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um evento")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        calendarEventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca os detalhes de um evento específico")
    public ResponseEntity<EntityModel<CalendarEventDetailsDto>> findById(@PathVariable Long id) {
        CalendarEventDetailsDto event = calendarEventService.findById(id);
        EntityModel<CalendarEventDetailsDto> resource = EntityModel.of(event,
                linkTo(methodOn(CalendarEventController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/month")
    @Operation(summary = "Busca todos os eventos e reuniões do usuário no mês e ano especificados")
    public ResponseEntity<CollectionModel<EntityModel<AgendaItemDto>>> findAgendaForMonth(
            @RequestParam int year,
            @RequestParam int month) {

        List<AgendaItemDto> agendaItems = calendarEventService.findVisibleEventsForUser(year, month);

        List<EntityModel<AgendaItemDto>> resources = agendaItems.stream()
                .map(item -> {
                    var itemLink = item.isMeeting()
                            ? linkTo(methodOn(MeetingController.class).findById(item.id())).withSelfRel()
                            : linkTo(methodOn(CalendarEventController.class).findById(item.id())).withSelfRel();
                    return EntityModel.of(item, itemLink);
                })
                .collect(Collectors.toList());

        var selfLink = linkTo(methodOn(CalendarEventController.class).findAgendaForMonth(year, month)).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(resources, selfLink));
    }

}
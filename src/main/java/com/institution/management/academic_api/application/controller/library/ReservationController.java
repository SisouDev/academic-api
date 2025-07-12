package com.institution.management.academic_api.application.controller.library;

import com.institution.management.academic_api.application.dto.library.CreateReservationRequestDto;
import com.institution.management.academic_api.application.dto.library.ReservationDetailsDto;
import com.institution.management.academic_api.domain.service.library.ReservationService;
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
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Tag(name = "Library Reservations", description = "Endpoints para gerenciamento de reservas de itens da biblioteca")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "Cria uma nova reserva para um item da biblioteca")
    public ResponseEntity<EntityModel<ReservationDetailsDto>> create(@RequestBody CreateReservationRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        ReservationDetailsDto createdReservation = reservationService.create(request, userEmail);
        EntityModel<ReservationDetailsDto> resource = EntityModel.of(createdReservation,
                linkTo(methodOn(ReservationController.class).findById(createdReservation.id())).withSelfRel());

        return ResponseEntity.created(URI.create(resource.getRequiredLink("self").getHref())).body(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancela uma reserva existente")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        reservationService.cancel(id, userEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca os detalhes de uma reserva pelo ID")
    public ResponseEntity<EntityModel<ReservationDetailsDto>> findById(@PathVariable Long id) {
        ReservationDetailsDto reservation = reservationService.findById(id);
        EntityModel<ReservationDetailsDto> resource = EntityModel.of(reservation,
                linkTo(methodOn(ReservationController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/my-reservations")
    @Operation(summary = "Lista todas as reservas ativas do usuário logado")
    public ResponseEntity<CollectionModel<EntityModel<ReservationDetailsDto>>> findMyActiveReservations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        List<ReservationDetailsDto> reservations = reservationService.findMyActiveReservations(userEmail);

        List<EntityModel<ReservationDetailsDto>> resources = reservations.stream()
                .map(reservation -> EntityModel.of(reservation,
                        linkTo(methodOn(ReservationController.class).findById(reservation.id())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resources,
                linkTo(methodOn(ReservationController.class).findMyActiveReservations()).withSelfRel()));
    }
}
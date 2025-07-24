package com.institution.management.academic_api.application.service.library;

import com.institution.management.academic_api.application.dto.library.CreateReservationRequestDto;
import com.institution.management.academic_api.application.dto.library.ReservationDetailsDto;
import com.institution.management.academic_api.application.mapper.simple.library.ReservationMapper;
import com.institution.management.academic_api.application.notifiers.library.ReservationNotifier;
import com.institution.management.academic_api.domain.factory.library.ReservationFactory;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.library.LibraryItem;
import com.institution.management.academic_api.domain.model.entities.library.Reservation;
import com.institution.management.academic_api.domain.model.enums.library.ReservationStatus;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.library.ReservationRepository;
import com.institution.management.academic_api.domain.service.library.ReservationService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final PersonRepository personRepository;
    private final ReservationFactory reservationFactory;
    private final ReservationMapper reservationMapper;
    private final ReservationNotifier reservationNotifier;

    @Override
    @Transactional
    @LogActivity("Criou uma nova reserva de item da biblioteca.")
    public ReservationDetailsDto create(CreateReservationRequestDto dto, String userEmail) {
        Person person = findPersonByEmailOrThrow(userEmail);

        CreateReservationRequestDto finalDto = new CreateReservationRequestDto(dto.itemId(), person.getId());

        if (reservationRepository.existsByPersonIdAndItemIdAndStatus(finalDto.personId(), finalDto.itemId(), ReservationStatus.ACTIVE)) {
            throw new IllegalStateException("You already have an active reservation for this item.");
        }
        Reservation newReservation = reservationFactory.create(finalDto);
        Reservation savedReservation = reservationRepository.save(newReservation);

        reservationNotifier.notifyUserOfNewReservation(savedReservation);

        return reservationMapper.toDetailsDto(savedReservation);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationDetailsDto findById(Long id) {
        return reservationRepository.findById(id)
                .map(reservationMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + id));
    }

    @Override
    @Transactional
    @LogActivity("Cancelou uma reserva de item da biblioteca.")
    public void cancel(Long reservationId, String userEmail) {
        Person person = findPersonByEmailOrThrow(userEmail);
        Reservation reservation = findReservationByIdOrThrow(reservationId);

        if (!reservation.getPerson().getId().equals(person.getId())) {
            throw new RuntimeException("You are not allowed to cancel this reservation.");
        }

        if(reservation.getStatus() != ReservationStatus.ACTIVE){
            throw new IllegalStateException("Only active reservations can be canceled.");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDetailsDto> findMyActiveReservations(String userEmail) {
        Person person = findPersonByEmailOrThrow(userEmail);

        return reservationRepository.findByPersonIdAndStatus(person.getId(), ReservationStatus.ACTIVE)
                .stream()
                .map(reservationMapper::toDetailsDto)
                .collect(Collectors.toList());
    }

    private Person findPersonByEmailOrThrow(String email) {
        return personRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    private Reservation findReservationByIdOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDetailsDto> findAllByStatus(ReservationStatus status, Pageable pageable) {
        return reservationRepository.findAllByStatus(status, pageable)
                .map(reservationMapper::toDetailsDto);
    }

    @Override
    @Transactional
    @LogActivity("Aprovou uma reserva de item da biblioteca.")
    public void approveReservation(Long reservationId) {
        Reservation reservation = findReservationByIdOrThrow(reservationId);

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Apenas reservas pendentes podem ser aprovadas.");
        }

        LibraryItem item = reservation.getItem();
        if (item.getAvailableCopies() <= 0) {
            throw new IllegalStateException("Não há cópias disponíveis do item '" + item.getTitle() + "' para atender a esta reserva.");
        }

        reservation.setStatus(ReservationStatus.AVAILABLE);

        reservationNotifier.notifyUserOfAvailableReservation(reservation);
    }
}

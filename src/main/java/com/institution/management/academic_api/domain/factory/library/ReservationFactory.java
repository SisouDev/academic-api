package com.institution.management.academic_api.domain.factory.library;

import com.institution.management.academic_api.application.dto.library.CreateReservationRequestDto;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.library.LibraryItem;
import com.institution.management.academic_api.domain.model.entities.library.Reservation;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.library.LibraryItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReservationFactory {

    private final PersonRepository personRepository;
    private final LibraryItemRepository libraryItemRepository;

    public Reservation create(CreateReservationRequestDto dto) {
        Person person = personRepository.findById(dto.personId())
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + dto.personId()));

        LibraryItem item = libraryItemRepository.findById(dto.itemId())
                .orElseThrow(() -> new EntityNotFoundException("Library item not found with id: " + dto.itemId()));

        if (item.getAvailableCopies() > 0) {
            throw new IllegalStateException("The item '" + item.getTitle() + "' is available for loan and cannot be reserved.");
        }

        Reservation reservation = new Reservation();
        reservation.setPerson(person);
        reservation.setItem(item);
        reservation.setReservationDate(LocalDateTime.now());

        return reservation;
    }
}
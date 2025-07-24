package com.institution.management.academic_api.domain.repository.library;

import com.institution.management.academic_api.domain.model.entities.library.Reservation;
import com.institution.management.academic_api.domain.model.enums.library.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByPersonId(Long personId);

    List<Reservation> findByItemIdOrderByReservationDateAsc(Long itemId);

    Optional<Reservation> findFirstByItemIdAndStatusOrderByReservationDateAsc(Long itemId, ReservationStatus status);

    List<Reservation> findByPersonIdAndStatus(Long id, ReservationStatus reservationStatus);

    boolean existsByPersonIdAndItemIdAndStatus(Long personId, Long itemId, ReservationStatus reservationStatus);

    Page<Reservation> findAllByStatus(ReservationStatus status, Pageable pageable);

}

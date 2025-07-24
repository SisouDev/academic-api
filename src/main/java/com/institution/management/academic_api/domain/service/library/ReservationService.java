package com.institution.management.academic_api.domain.service.library;

import com.institution.management.academic_api.application.dto.library.CreateReservationRequestDto;
import com.institution.management.academic_api.application.dto.library.ReservationDetailsDto;
import com.institution.management.academic_api.domain.model.enums.library.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationService {
    ReservationDetailsDto create(CreateReservationRequestDto dto, String userEmail);

    void cancel(Long reservationId, String userEmail);

    List<ReservationDetailsDto> findMyActiveReservations(String userEmail);

    ReservationDetailsDto findById(Long id);

    Page<ReservationDetailsDto> findAllByStatus(ReservationStatus status, Pageable pageable);

    void approveReservation(Long reservationId);
}

package com.institution.management.academic_api.domain.service.library;

import com.institution.management.academic_api.application.dto.library.CreateReservationRequestDto;
import com.institution.management.academic_api.application.dto.library.ReservationDetailsDto;

import java.util.List;

public interface ReservationService {
    ReservationDetailsDto create(CreateReservationRequestDto dto, String userEmail);

    void cancel(Long reservationId, String userEmail);

    List<ReservationDetailsDto> findMyActiveReservations(String userEmail);

    ReservationDetailsDto findById(Long id);
}

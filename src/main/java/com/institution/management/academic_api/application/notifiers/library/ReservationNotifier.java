package com.institution.management.academic_api.application.notifiers.library;

import com.institution.management.academic_api.domain.model.entities.library.LibraryItem;
import com.institution.management.academic_api.domain.model.entities.library.Reservation;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.model.enums.library.ReservationStatus;
import com.institution.management.academic_api.domain.repository.library.ReservationRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationNotifier {

    private final NotificationService notificationService;
    private final ReservationRepository reservationRepository;

    public void notifyUserOfNewReservation(Reservation reservation) {
        String message = String.format("Sua reserva para o item '%s' foi confirmada.", reservation.getItem().getTitle());
        String link = "/my-profile/reservations";

        notificationService.createNotification(
                reservation.getPerson().getUser(),
                message,
                link,
                NotificationType.SUCCESS
        );
    }

    public void notifyNextInQueue(LibraryItem returnedItem) {
        reservationRepository.findFirstByItemIdAndStatusOrderByReservationDateAsc(
                returnedItem.getId(), ReservationStatus.ACTIVE
        ).ifPresent(nextReservation -> {

            nextReservation.setStatus(ReservationStatus.AVAILABLE);
            reservationRepository.save(nextReservation);

            String message = String.format(
                    "O item '%s' que você reservou está disponível para retirada na biblioteca!",
                    returnedItem.getTitle()
            );
            String link = "/my-profile/reservations";

            notificationService.createNotification(
                    nextReservation.getPerson().getUser(),
                    message,
                    link,
                    NotificationType.ACTION_REQUIRED
            );
        });
    }
}
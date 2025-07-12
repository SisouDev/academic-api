package com.institution.management.academic_api.application.dto.library;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representação detalhada de uma reserva.")
public record ReservationDetailsDto(
        @Schema(description = "ID único da reserva.", example = "1")
        Long id,

        @Schema(description = "Data e hora em que a reserva foi feita.")
        LocalDateTime reservationDate,

        @Schema(description = "Status atual da reserva.", example = "Ativa")
        String status,

        @Schema(description = "Item que foi reservado.")
        LibraryItemSummaryDto item,

        @Schema(description = "Pessoa que fez a reserva.")
        PersonSummaryDto person
) {}
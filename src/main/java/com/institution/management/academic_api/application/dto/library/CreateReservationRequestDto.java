package com.institution.management.academic_api.application.dto.library;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para criar uma nova reserva para um item.")
public record CreateReservationRequestDto(
        @Schema(description = "ID do item da biblioteca a ser reservado.", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        Long itemId,

        @Schema(description = "ID da pessoa que está fazendo a reserva.", requiredMode = Schema.RequiredMode.REQUIRED, example = "102")
        Long personId
) {}
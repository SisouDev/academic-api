package com.institution.management.academic_api.application.dto.humanResources;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Representação detalhada de uma solicitação de ausência.")
public record LeaveRequestDetailsDto(
        @Schema(description = "ID único da solicitação.", example = "1")
        Long id,

        @Schema(description = "Tipo da solicitação.", example = "Férias")
        String type,

        @Schema(description = "Data de início do período.")
        LocalDate startDate,

        @Schema(description = "Data de término do período.")
        LocalDate endDate,

        @Schema(description = "Motivo da solicitação.")
        String reason,

        @Schema(description = "Status da solicitação.", example = "Aprovado")
        String status,

        @Schema(description = "Data e hora em que a solicitação foi feita.")
        LocalDateTime createdAt,

        @Schema(description = "Data e hora em que a solicitação foi analisada.")
        LocalDateTime reviewedAt,

        @Schema(description = "Funcionário que fez a solicitação.")
        PersonSummaryDto requester,

        @Schema(description = "Funcionário do RH que analisou a solicitação.")
        PersonSummaryDto reviewer
) {}
package com.institution.management.academic_api.application.dto.meeting;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "Representação detalhada de uma reunião.")
public record MeetingDetailsDto(
        @Schema(description = "ID único da reunião.", example = "1")
        Long id,

        @Schema(description = "Título da reunião.", example = "Reunião de Alinhamento Semestral")
        String title,

        @Schema(description = "Descrição ou pauta da reunião.")
        String description,

        @Schema(description = "Data e hora de início.")
        LocalDateTime startTime,

        @Schema(description = "Data e hora de término.")
        LocalDateTime endTime,

        @Schema(description = "Visibilidade da reunião.", example = "PRIVATE")
        String visibility,

        @Schema(description = "Data de criação do agendamento.")
        LocalDateTime createdAt,

        @Schema(description = "Organizador da reunião.")
        PersonSummaryDto organizer,

        @Schema(description = "Lista de participantes da reunião e seus status de resposta.")
        Set<MeetingParticipantDto> participants

) {}
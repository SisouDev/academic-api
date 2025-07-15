package com.institution.management.academic_api.application.dto.meeting;
import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa um participante e seu status de resposta em uma reunião.")
public record MeetingParticipantDto(
        @Schema(description = "ID da relação de participação.")
        Long id,

        @Schema(description = "Dados resumidos do participante.")
        PersonSummaryDto participant,

        @Schema(description = "Status de resposta ao convite (RSVP).")
        String status
) {}
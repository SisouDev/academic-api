package com.institution.management.academic_api.application.dto.meeting;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "Dados para atualizar uma reunião. Apenas os campos fornecidos serão alterados.")
public record UpdateMeetingRequestDto(
        @Schema(description = "Novo título da reunião.")
        String title,

        @Schema(description = "Nova descrição ou pauta.")
        String description,

        @Schema(description = "Nova data e hora de início.")
        LocalDateTime startTime,

        @Schema(description = "Nova data e hora de término.")
        LocalDateTime endTime,

        @Schema(description = "Nova visibilidade da reunião.")
        String visibility,

        @Schema(description = "Nova lista de IDs de participantes. Pode ser usada para adicionar ou remover convidados.")
        Set<Long> participantIds
) {}
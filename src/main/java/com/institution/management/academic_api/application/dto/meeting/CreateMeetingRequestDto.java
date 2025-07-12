package com.institution.management.academic_api.application.dto.meeting;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "Dados para agendar uma nova reunião.")
public record CreateMeetingRequestDto(
        @Schema(description = "Título da reunião.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Reunião de Alinhamento Semestral")
        String title,

        @Schema(description = "Descrição ou pauta da reunião.", example = "Discutir as metas para o próximo semestre.")
        String description,

        @Schema(description = "Data e hora de início.", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-08-01T10:00:00")
        LocalDateTime startTime,

        @Schema(description = "Data e hora de término.", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-08-01T11:30:00")
        LocalDateTime endTime,

        @Schema(description = "Visibilidade da reunião.", requiredMode = Schema.RequiredMode.REQUIRED, example = "PRIVATE")
        String visibility,

        @Schema(description = "Conjunto de IDs das pessoas (funcionários/professores) convidadas para a reunião.")
        Set<Long> participantIds
) {}
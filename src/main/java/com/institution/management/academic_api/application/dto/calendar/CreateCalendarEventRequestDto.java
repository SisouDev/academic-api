package com.institution.management.academic_api.application.dto.calendar;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Dados para criar um novo evento no calendário.")
public record CreateCalendarEventRequestDto(
        @Schema(description = "Título do evento.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Treinamento de Primeiros Socorros")
        String title,

        @Schema(description = "Descrição detalhada do evento.", example = "Treinamento obrigatório para todos os funcionários do setor de esportes.")
        String description,

        @Schema(description = "Tipo do evento.", requiredMode = Schema.RequiredMode.REQUIRED, example = "TRAINING")
        String type,

        @Schema(description = "Data e hora de início do evento.", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-10-15T09:00:00")
        LocalDateTime startTime,

        @Schema(description = "Data e hora de término do evento.", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-10-15T12:00:00")
        LocalDateTime endTime,

        @Schema(description = "Escopo de visibilidade do evento.", requiredMode = Schema.RequiredMode.REQUIRED, example = "INSTITUTION")
        String scope,

        @Schema(description = "ID do departamento alvo, se o escopo for 'DEPARTMENT'.", example = "5")
        Long targetDepartmentId
) {}
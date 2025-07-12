package com.institution.management.academic_api.application.dto.dashboard.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Representa um item futuro na agenda do professor (avaliação, reunião, etc.).")
public record UpcomingTaskInfo(
        @Schema(description = "Título da tarefa ou evento.")
        String title,

        @Schema(description = "Tipo da tarefa para diferenciação na UI (ex: 'Avaliação', 'Reunião').")
        String type,

        @Schema(description = "Data e hora do início da tarefa/evento.")
        LocalDateTime date,

        @Schema(description = "Contexto da tarefa, como o nome da turma ou matéria.")
        String context
) {}
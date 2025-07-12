package com.institution.management.academic_api.application.dto.dashboard.admin;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Representa um item no feed de atividades recentes.")
public record ActivityFeedItem(
        @Schema(description = "Tipo da atividade (ex: NEW_STUDENT, COURSE_CREATED).")
        String type,
        @Schema(description = "Descrição da atividade.")
        String description,
        @Schema(description = "Data e hora em que a atividade ocorreu.")
        LocalDateTime timestamp
) {}
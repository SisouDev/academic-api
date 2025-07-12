package com.institution.management.academic_api.application.dto.tasks;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Dados para atualizar uma tarefa existente.")
public record UpdateTaskRequestDto(
        @Schema(description = "Novo título da tarefa.", example = "Relatório FINAL de desempenho do semestre")
        String title,

        @Schema(description = "Nova descrição da tarefa.")
        String description,

        @Schema(description = "Nova data de prazo.", example = "2025-12-22")
        LocalDate dueDate,

        @Schema(description = "Novo status da tarefa.", example = "IN_PROGRESS")
        String status,

        @Schema(description = "Novo funcionário responsável pela tarefa.", example = "23")
        Long assigneeId
) {}
package com.institution.management.academic_api.application.dto.tasks;

import com.institution.management.academic_api.application.dto.academic.DepartmentSummaryDto;
import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Representação detalhada de uma tarefa.")
public record TaskDetailsDto(
        @Schema(description = "ID único da tarefa.", example = "51")
        Long id,

        @Schema(description = "Título da tarefa.", example = "Elaborar relatório de desempenho")
        String title,

        @Schema(description = "Descrição completa da tarefa.")
        String description,

        @Schema(description = "Prazo final para conclusão.", example = "2025-12-20")
        LocalDate dueDate,

        @Schema(description = "Status atual da tarefa.", example = "TODO")
        String status,

        @Schema(description = "Data e hora de criação.")
        LocalDateTime createdAt,

        @Schema(description = "Data e hora da conclusão.")
        LocalDateTime completedAt,

        @Schema(description = "Funcionário que criou a tarefa.")
        PersonSummaryDto createdBy,

        @Schema(description = "Funcionário responsável pela execução.")
        PersonSummaryDto assignee,

        @Schema(description = "Departamento ao qual a tarefa pertence.")
        DepartmentSummaryDto department
) {}
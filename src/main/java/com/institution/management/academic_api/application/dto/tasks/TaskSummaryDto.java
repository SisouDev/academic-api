package com.institution.management.academic_api.application.dto.tasks;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Representação resumida de uma tarefa.")
public record TaskSummaryDto(
        @Schema(description = "ID único da tarefa.", example = "51")
        Long id,

        @Schema(description = "Título da tarefa.", example = "Elaborar relatório de desempenho")
        String title,

        @Schema(description = "Status atual da tarefa.", example = "TODO")
        String status,

        @Schema(description = "Prazo final para conclusão.", example = "2025-12-20")
        LocalDate dueDate,

        @Schema(description = "Nome do funcionário responsável.", example = "Carlos Mendes")
        String assigneeName,

        @Schema(description = "Nome do departamento.", example = "Coordenação de Engenharia")
        String departmentName
) {}
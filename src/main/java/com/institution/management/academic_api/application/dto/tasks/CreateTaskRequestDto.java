package com.institution.management.academic_api.application.dto.tasks;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Dados para criar uma nova tarefa.")
public record CreateTaskRequestDto(
        @Schema(description = "Título breve da tarefa.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Elaborar relatório de desempenho do semestre")
        String title,

        @Schema(description = "Descrição completa da tarefa e seus requisitos.", example = "O relatório deve conter as métricas X, Y e Z.")
        String description,

        @Schema(description = "Prazo final para a conclusão da tarefa.", example = "2025-12-20")
        LocalDate dueDate,

        @Schema(description = "ID do departamento ao qual a tarefa pertence.", requiredMode = Schema.RequiredMode.REQUIRED, example = "4")
        Long departmentId,

        @Schema(description = "ID do funcionário (Employee) ao qual a tarefa será atribuída. Pode ser nulo.", example = "22")
        Long assigneeId
) {}
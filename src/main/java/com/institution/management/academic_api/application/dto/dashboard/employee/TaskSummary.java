package com.institution.management.academic_api.application.dto.dashboard.employee;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Resumo de uma tarefa.")
public record TaskSummary(
        Long id,
        String title,
        LocalDate dueDate,
        String status
) {}
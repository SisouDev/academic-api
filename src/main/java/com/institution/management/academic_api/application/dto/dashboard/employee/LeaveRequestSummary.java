package com.institution.management.academic_api.application.dto.dashboard.employee;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Resumo de um pedido de licença.")
public record LeaveRequestSummary(
        Long id,
        String requesterName,
        String type,
        LocalDate startDate,
        LocalDate endDate,
        String status
) {}
package com.institution.management.academic_api.application.dto.employee;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação RESUMIDA de um funcionário para listas.")
public record EmployeeSummaryDto(
        @Schema(description = "ID da pessoa.", example = "205")
        Long id,

        @Schema(description = "Nome completo do funcionário.", example = "Roberto Gomes")
        String fullName,

        @Schema(description = "Email de contato.", example = "roberto.gomes@example.com")
        String email,

        @Schema(description = "Cargo do funcionário.", example = "COORDINATOR")
        String jobPosition
) {}
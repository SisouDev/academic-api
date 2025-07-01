package com.institution.management.academic_api.application.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizar parcialmente um Funcionário. Apenas os campos fornecidos serão alterados.")
public record UpdateEmployeeRequestDto(
        @Schema(description = "Novo email de contato.", example = "roberto.gomes.new@example.com")
        String email,

        @Schema(description = "Novo telefone de contato.", example = "+55 (21) 12345-6789")
        String phone,

        @Schema(description = "Novo status do funcionário no sistema.", example = "INACTIVE")
        String status,

        @Schema(description = "Novo cargo do funcionário.", example = "SENIOR_COORDINATOR")
        String jobPosition,

        String firstName,
        String lastName,

        @Schema(description = "Novo ID do departamento.", example = "2")
        Long departmentId
) {}
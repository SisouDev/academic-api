package com.institution.management.academic_api.application.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Representação de um funcionário para listas de gerenciamento.")
public record EmployeeListDto(
        @Schema(description = "ID da pessoa.", example = "205")
        Long id,

        @Schema(description = "URL da foto de perfil.")
        String profilePictureUrl,

        @Schema(description = "Nome completo do funcionário.", example = "Roberto Gomes")
        String fullName,

        @Schema(description = "Email de contato.", example = "roberto.gomes@example.com")
        String email,

        @Schema(description = "Cargo do funcionário.", example = "COORDINATOR")
        String jobPosition,

        @Schema(description = "Data de contratação.", example = "2022-05-20")
        LocalDate hiringDate,

        BigDecimal baseSalary

) {}
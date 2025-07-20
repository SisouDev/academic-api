package com.institution.management.academic_api.application.dto.common;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record UpdateSalaryStructureRequestDto(
        @NotNull(message = "O salário base não pode ser nulo.")
        @Positive(message = "O salário base deve ser um valor positivo.")
        BigDecimal baseSalary
) {}
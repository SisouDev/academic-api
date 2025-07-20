package com.institution.management.academic_api.application.dto.common;

import com.institution.management.academic_api.domain.model.enums.common.SalaryLevel;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateSalaryStructureRequestDto(
        @NotNull(message = "O cargo não pode ser nulo.")
        JobPosition jobPosition,

        @NotNull(message = "O nível não pode ser nulo.")
        SalaryLevel level,

        @NotNull(message = "O salário base não pode ser nulo.")
        @Positive(message = "O salário base deve ser um valor positivo.")
        BigDecimal baseSalary
) {}
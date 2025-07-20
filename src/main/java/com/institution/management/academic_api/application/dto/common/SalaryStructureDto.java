package com.institution.management.academic_api.application.dto.common;

import com.institution.management.academic_api.domain.model.enums.common.SalaryLevel;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;

import java.math.BigDecimal;

public record SalaryStructureDto(
        Long id,
        JobPosition jobPosition,
        SalaryLevel level,
        BigDecimal baseSalary
) {}
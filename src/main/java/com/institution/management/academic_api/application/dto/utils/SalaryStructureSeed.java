package com.institution.management.academic_api.application.dto.utils;

import com.institution.management.academic_api.domain.model.enums.common.SalaryLevel;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;

import java.math.BigDecimal;

public record SalaryStructureSeed(String key, JobPosition position, SalaryLevel level, BigDecimal salary) {}


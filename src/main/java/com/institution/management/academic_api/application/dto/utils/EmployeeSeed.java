package com.institution.management.academic_api.application.dto.utils;

import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import com.institution.management.academic_api.domain.model.enums.common.SalaryLevel;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;

import java.time.LocalDate;
import java.util.Set;

public record EmployeeSeed(
        String key,
        String firstName,
        String lastName,
        String email,
        JobPosition position,
        SalaryLevel level,
        LocalDate hiringDate,
        String documentNumber,
        Set<RoleName> roles
) {}
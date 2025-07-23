package com.institution.management.academic_api.application.dto.employee;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StaffListDto(
        Long id,
        String profilePictureUrl,
        String fullName,
        String email,
        String positionOrDegree,
        LocalDate hiringDate,
        BigDecimal baseSalary
) {}
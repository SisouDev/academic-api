package com.institution.management.academic_api.application.dto.financial;

import com.institution.management.academic_api.domain.model.enums.financial.DiscountType;
import com.institution.management.academic_api.domain.model.enums.financial.ScholarshipStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ScholarshipDetailsDto(
        Long id,
        Long enrollmentId,
        String studentName,
        String scholarshipName,
        DiscountType discountType,
        BigDecimal value,
        ScholarshipStatus status,
        LocalDate startDate,
        LocalDate endDate
) {}
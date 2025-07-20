package com.institution.management.academic_api.application.service.utils;

import com.institution.management.academic_api.domain.model.entities.common.StaffMember;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.enums.teacher.AcademicDegree;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BonusCalculationService {

    private static final BigDecimal DOCTORATE_BONUS = new BigDecimal("1500.00");
    private static final BigDecimal MASTER_BONUS = new BigDecimal("750.00");
    private static final BigDecimal SPECIALIZATION_BONUS = new BigDecimal("300.00");

    public BigDecimal calculateBonuses(StaffMember staffMember) {
        BigDecimal totalBonus = BigDecimal.ZERO;

        if (staffMember instanceof Teacher teacher) {
            AcademicDegree degree = teacher.getAcademicBackground();

            if (degree != null) {
                totalBonus = switch (degree) {
                    case PHD -> totalBonus.add(DOCTORATE_BONUS);
                    case MASTER -> totalBonus.add(MASTER_BONUS);
                    case SPECIALIZATION -> totalBonus.add(SPECIALIZATION_BONUS);
                    default -> totalBonus;
                };
            }
        }

        return totalBonus;
    }
}
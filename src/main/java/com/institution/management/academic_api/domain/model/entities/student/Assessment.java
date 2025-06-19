package com.institution.management.academic_api.domain.model.entities.student;

import com.institution.management.academic_api.domain.model.enums.student.AssessmentType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Assessment {
    private Long id;
    private BigDecimal score;
    private LocalDate assessmentDate;
    private AssessmentType type;
}

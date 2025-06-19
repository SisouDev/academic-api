package com.institution.management.academic_api.domain.model.entities.academic;

import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;

import java.time.LocalDate;
import java.time.Year;

public class AcademicTerm {
    private Long id;
    private Year year;
    private Integer semester;
    private LocalDate startDate;
    private LocalDate endDate;
    private AcademicTermStatus status;
}

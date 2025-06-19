package com.institution.management.academic_api.domain.model.entities.academic;

import java.time.LocalDate;
import java.time.Year;

public class AcademicTerm {
    private Long id;
    private Year year;
    private Integer semester;
    private LocalDate startDate;
    private LocalDate endDate;
}

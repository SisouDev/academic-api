package com.institution.management.academic_api.domain.model.entities.student;

import com.institution.management.academic_api.domain.model.enums.student.EnrollmentStatus;

import java.time.LocalDate;

public class Enrollment {
    private Long id;
    private LocalDate enrollmentDate;
    private Integer totalAbsences;
    private EnrollmentStatus status;
}

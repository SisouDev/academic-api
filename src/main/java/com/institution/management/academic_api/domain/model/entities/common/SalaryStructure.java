package com.institution.management.academic_api.domain.model.entities.common;

import com.institution.management.academic_api.domain.model.enums.common.SalaryLevel;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "salary_structures", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"job_position", "level"})
})
@Getter
@Setter
public class SalaryStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_position", nullable = false)
    private JobPosition jobPosition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalaryLevel level;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal baseSalary;
}
package com.institution.management.academic_api.domain.model.entities.employee;

import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Employee extends Person {

    @Enumerated(EnumType.STRING)
    private JobPosition jobPosition;

    @Column
    private LocalDate hiringDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @ToString.Exclude
    private Department department;

    @Override
    public PersonType getPersonType() {
        return PersonType.EMPLOYEE;
    }
}

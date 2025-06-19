package com.institution.management.academic_api.domain.model.entities.employee;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;

import java.time.LocalDate;

public class Employee extends Person {

    private JobPosition jobPosition;
    private LocalDate hiringDate;

    @Override
    public PersonType getPersonType() {
        return PersonType.EMPLOYEE;
    }
}

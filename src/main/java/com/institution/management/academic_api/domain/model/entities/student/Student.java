package com.institution.management.academic_api.domain.model.entities.student;

import com.institution.management.academic_api.domain.model.entities.common.Address;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;

import java.time.LocalDate;

public class Student extends Person {

    private LocalDate birthDate;
    private Address address;

    @Override
    public PersonType getPersonType() {
        return PersonType.STUDENT;
    }
}

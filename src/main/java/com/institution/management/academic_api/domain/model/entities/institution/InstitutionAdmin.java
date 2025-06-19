package com.institution.management.academic_api.domain.model.entities.institution;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;

public class InstitutionAdmin extends Person {
    private Long id;

    @Override
    public PersonType getPersonType() {
        return PersonType.STAFF;
    }
}

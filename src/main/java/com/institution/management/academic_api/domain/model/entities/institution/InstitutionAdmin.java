package com.institution.management.academic_api.domain.model.entities.institution;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "institution_admins")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class InstitutionAdmin extends Person {

    @Override
    public PersonType getPersonType() {
        return PersonType.STAFF;
    }
}

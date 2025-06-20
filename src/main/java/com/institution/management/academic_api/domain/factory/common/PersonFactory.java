package com.institution.management.academic_api.domain.factory.common;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;

public interface PersonFactory {
    Person create(Object requestDto);

    PersonType supportedType();
}
package com.institution.management.academic_api.domain.model.entities.teacher;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import com.institution.management.academic_api.domain.model.enums.teacher.AcademicDegree;

public class Teacher extends Person {
    private AcademicDegree academicBackground;

    @Override
    public PersonType getPersonType() {
        return PersonType.TEACHER;
    }
}

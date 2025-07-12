package com.institution.management.academic_api.domain.model.enums.common;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.institution.InstitutionAdmin;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum PersonType {
    STUDENT("Student", Student.class),
    TEACHER("Teacher", Teacher.class),
    STAFF("Staff", InstitutionAdmin.class),
    EMPLOYEE("Employee", Employee.class);

    private final String displayName;
    private final Class<? extends Person> entityClass;

    PersonType(String displayName, Class<? extends Person> entityClass) {
        this.displayName = displayName;
        this.entityClass = entityClass;
    }

    public static PersonType fromDisplayName(String text) {
        return Stream.of(PersonType.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}

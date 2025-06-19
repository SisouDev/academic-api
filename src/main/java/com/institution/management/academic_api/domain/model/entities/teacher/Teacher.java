package com.institution.management.academic_api.domain.model.entities.teacher;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import com.institution.management.academic_api.domain.model.enums.teacher.AcademicDegree;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Teacher extends Person {

    @Enumerated(EnumType.STRING)
    private AcademicDegree academicBackground;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CourseSection> courseSections = new ArrayList<>();

    @Override
    public PersonType getPersonType() {
        return PersonType.TEACHER;
    }
}

package com.institution.management.academic_api.domain.model.entities.teacher;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.course.Subject;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import com.institution.management.academic_api.domain.model.enums.teacher.AcademicDegree;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "teacher_subjects",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @ToString.Exclude
    private Set<Subject> subjects = new HashSet<>();

    @Override
    public PersonType getPersonType() {
        return PersonType.TEACHER;
    }
}

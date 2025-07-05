package com.institution.management.academic_api.domain.model.entities.course;

import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
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
@Table(name = "subjects")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer workloadHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @ToString.Exclude
    private Course course;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CourseSection> courseSections = new ArrayList<>();

    @ManyToMany(mappedBy = "subjects")
    @ToString.Exclude
    private Set<Teacher> teachers = new HashSet<>();
}

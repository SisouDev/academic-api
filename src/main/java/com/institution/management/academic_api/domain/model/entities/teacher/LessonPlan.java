package com.institution.management.academic_api.domain.model.entities.teacher;

import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "lesson_plans")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class LessonPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String objectives;

    @Column
    private String syllabusContent;

    @Column
    private String bibliography;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "course_section_id",
            nullable = false,
            unique = true
    )
    @ToString.Exclude
    private CourseSection courseSection;

}

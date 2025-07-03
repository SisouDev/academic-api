package com.institution.management.academic_api.domain.model.entities.student;

import com.institution.management.academic_api.domain.model.enums.student.AssessmentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "assessments")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private BigDecimal score;

    @Column
    private LocalDate assessmentDate;

    @Enumerated(EnumType.STRING)
    private AssessmentType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    @ToString.Exclude
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_definition_id", nullable = false)
    @ToString.Exclude
    private AssessmentDefinition assessmentDefinition;
}

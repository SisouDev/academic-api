package com.institution.management.academic_api.domain.repository.student;

import com.institution.management.academic_api.domain.model.entities.student.Assessment;
import com.institution.management.academic_api.domain.model.entities.student.AssessmentDefinition;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.enums.student.AssessmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByEnrollmentOrderByAssessmentDate(Enrollment enrollment);

    List<Assessment> findByEnrollmentAndType(Enrollment enrollment, AssessmentType type);

    List<Assessment> findAllByEnrollment(Enrollment enrollment);

    boolean existsByEnrollmentAndAssessmentDefinition(Enrollment enrollment, AssessmentDefinition examDef);

    Optional<Assessment> findByEnrollmentAndAssessmentDefinition(Enrollment enrollment, AssessmentDefinition definition);
}

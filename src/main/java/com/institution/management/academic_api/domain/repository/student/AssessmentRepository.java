package com.institution.management.academic_api.domain.repository.student;

import com.institution.management.academic_api.domain.model.entities.student.Assessment;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.enums.student.AssessmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByEnrollmentOrderByAssessmentDate(Enrollment enrollment);

    List<Assessment> findByEnrollmentAndType(Enrollment enrollment, AssessmentType type);
}

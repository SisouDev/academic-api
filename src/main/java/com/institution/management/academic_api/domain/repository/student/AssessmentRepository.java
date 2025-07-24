package com.institution.management.academic_api.domain.repository.student;

import com.institution.management.academic_api.domain.model.entities.student.Assessment;
import com.institution.management.academic_api.domain.model.entities.student.AssessmentDefinition;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.enums.student.AssessmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByEnrollmentOrderByAssessmentDate(Enrollment enrollment);

    List<Assessment> findByEnrollmentAndType(Enrollment enrollment, AssessmentType type);

    List<Assessment> findAllByEnrollment(Enrollment enrollment);

    boolean existsByEnrollmentAndAssessmentDefinition(Enrollment enrollment, AssessmentDefinition examDef);

    Optional<Assessment> findByEnrollmentAndAssessmentDefinition(Enrollment enrollment, AssessmentDefinition definition);

    @Query("SELECT AVG(a.score) FROM Assessment a " +
            "WHERE a.enrollment.student.id = :studentId")
    BigDecimal findAverageScoreByStudent(@Param("studentId") Long studentId);

    Page<Assessment> findTop3ByEnrollment_StudentOrderByAssessmentDateDesc(Student student, Pageable pageable);

    @Query("SELECT AVG(a.score) FROM Assessment a WHERE a.enrollment = :enrollment")
    BigDecimal findAverageScoreByEnrollment(@Param("enrollment") Enrollment enrollment);

    @Query("SELECT AVG(a.score) FROM Assessment a WHERE a.score IS NOT NULL")
    Optional<BigDecimal> findOverallAverageScore();

    @Query("SELECT (CAST(SUM(CASE WHEN a.score >= :passingGrade THEN 1 ELSE 0 END) AS double) / COUNT(a.id)) * 100.0 FROM Assessment a")
    Optional<BigDecimal> findOverallApprovalRate(@Param("passingGrade") BigDecimal passingGrade);
}

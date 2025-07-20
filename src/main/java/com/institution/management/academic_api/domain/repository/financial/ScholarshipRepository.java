package com.institution.management.academic_api.domain.repository.financial;

import com.institution.management.academic_api.domain.model.entities.financial.Scholarship;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {

    @Query("SELECT s FROM Scholarship s WHERE s.enrollment = :enrollment AND s.status = 'ACTIVE' " +
            "AND (s.startDate IS NULL OR s.startDate <= :currentDate) " +
            "AND (s.endDate IS NULL OR s.endDate >= :currentDate)")
    Optional<Scholarship> findActiveForEnrollment(Enrollment enrollment, LocalDate currentDate);

    List<Scholarship> findByEnrollmentId(Long enrollmentId);
}
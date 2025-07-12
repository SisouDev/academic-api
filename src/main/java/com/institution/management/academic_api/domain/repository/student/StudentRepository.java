package com.institution.management.academic_api.domain.repository.student;

import com.institution.management.academic_api.application.dto.dashboard.admin.StudentDistributionData;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Student> findAllByInstitution(Institution institution);

    @Modifying
    @Query("UPDATE Person p SET p.status = :status WHERE p.id = :id")
    void updateStudentStatus(@Param("id") Long id, @Param("status") PersonStatus status);

    long countByStatus(PersonStatus status);

    @Query("SELECT new com.institution.management.academic_api.application.dto.dashboard.admin.StudentDistributionData(cs.subject.course.name, COUNT(e.student.id)) " +
            "FROM Enrollment e " +
            "JOIN e.courseSection cs " +
            "WHERE e.status = 'ACTIVE' " +
            "GROUP BY cs.subject.course.name " +
            "ORDER BY COUNT(e.student.id) DESC")
    List<StudentDistributionData> findStudentDistributionByCourse();

    List<Student> findTop5ByOrderByCreatedAtDesc();
}

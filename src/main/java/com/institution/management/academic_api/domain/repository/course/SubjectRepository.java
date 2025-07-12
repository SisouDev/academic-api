package com.institution.management.academic_api.domain.repository.course;

import com.institution.management.academic_api.domain.model.entities.course.Course;
import com.institution.management.academic_api.domain.model.entities.course.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByCourse(Course course);

    List<Subject> findByCourseId(Long courseId);

    Optional<Subject> findByNameAndCourse(String name, Course course);

    List<Subject> findByWorkloadHoursGreaterThan(Integer hours);

    boolean existsByNameAndCourse(String name, Course course);

    List<Subject> findAllByCourse(Course course);

    @Query("SELECT s FROM Subject s JOIN s.courseSections cs JOIN cs.enrollments e WHERE e.student.id = :studentId AND LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Subject> findEnrolledSubjectsByStudentIdAndName(@Param("studentId") Long studentId, @Param("searchTerm") String searchTerm);

    List<Subject> findByNameContainingIgnoreCase(String name);
}

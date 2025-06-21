package com.institution.management.academic_api.domain.repository.course;

import com.institution.management.academic_api.domain.model.entities.course.Course;
import com.institution.management.academic_api.domain.model.entities.course.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
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
}

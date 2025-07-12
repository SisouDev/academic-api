package com.institution.management.academic_api.domain.repository.student;

import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.student.AssessmentDefinition;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AssessmentDefinitionRepository extends JpaRepository<AssessmentDefinition, Long> {
    List<AssessmentDefinition> findAllByCourseSectionId(Long courseSectionId);

    Optional<AssessmentDefinition> findByTitleAndCourseSection(String title, CourseSection courseSection);

    @Query("SELECT ad FROM AssessmentDefinition ad " +
            "JOIN ad.courseSection cs " +
            "JOIN cs.enrollments e " +
            "WHERE e.student = :student " +
            "AND ad.assessmentDate > :date " +
            "ORDER BY ad.assessmentDate ASC " +
            "LIMIT 1")
    Optional<AssessmentDefinition> findNextAssessmentForStudent(@Param("student") Student student, @Param("date") LocalDate date);

    @Query("SELECT ad FROM AssessmentDefinition ad " +
            "WHERE ad.courseSection.teacher = :teacher " +
            "AND ad.assessmentDate > :date " +
            "ORDER BY ad.assessmentDate ASC")
    List<AssessmentDefinition> findUpcomingAssessmentsForTeacher(
            @Param("teacher") Teacher teacher,
            @Param("date") LocalDate date,
            Pageable pageable);
}
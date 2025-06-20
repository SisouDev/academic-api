package com.institution.management.academic_api.domain.repository.teacher;

import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.teacher.LessonPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonPlanRepository extends JpaRepository<LessonPlan, Long> {
    Optional<LessonPlan> findByCourseSection(CourseSection courseSection);

    Optional<LessonPlan> findByCourseSectionId(Long courseSectionId);
}

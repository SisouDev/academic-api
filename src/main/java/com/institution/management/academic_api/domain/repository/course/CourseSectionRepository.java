package com.institution.management.academic_api.domain.repository.course;

import com.institution.management.academic_api.domain.model.entities.academic.AcademicTerm;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.course.Subject;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {
    List<CourseSection> findByAcademicTerm(AcademicTerm term);

    List<CourseSection> findByTeacherAndAcademicTerm(Teacher teacher, AcademicTerm term);

    List<CourseSection> findBySubjectAndAcademicTerm(Subject subject, AcademicTerm term);

    List<CourseSection> findByRoomAndAcademicTerm(String room, AcademicTerm term);

    boolean existsCourseSectionByNameAndAcademicTerm(String name, AcademicTerm academicTerm);

    List<CourseSection> findAllByAcademicTerm(AcademicTerm academicTerm);

    Optional<CourseSection> findByNameAndAcademicTerm(String courseSectionName, AcademicTerm term);
}

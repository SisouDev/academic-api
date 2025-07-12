package com.institution.management.academic_api.domain.repository.course;

import com.institution.management.academic_api.domain.model.entities.academic.AcademicTerm;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.course.Subject;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<CourseSection> findByTeacherAndAcademicTerm_Status(Teacher teacher, AcademicTermStatus status);

    @Query("SELECT cs FROM CourseSection cs LEFT JOIN FETCH cs.enrollments WHERE cs.teacher = :teacher AND cs.academicTerm.status IN :statuses")
    List<CourseSection> findSectionsByTeacherAndTermStatusIn(
            @Param("teacher") Teacher teacher,
            @Param("statuses") List<AcademicTermStatus> statuses);

    @Query("SELECT cs FROM CourseSection cs WHERE cs.subject.id = :subjectId AND cs.academicTerm.status IN :statuses ORDER BY cs.academicTerm.startDate DESC")
    Optional<CourseSection> findTopBySubjectIdAndAcademicTerm_StatusIn(
            @Param("subjectId") Long subjectId,
            @Param("statuses") List<AcademicTermStatus> statuses);
}

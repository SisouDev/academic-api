package com.institution.management.academic_api.domain.repository.student;

import com.institution.management.academic_api.domain.model.entities.academic.AcademicTerm;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.enums.student.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(Student student);

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseSection(CourseSection courseSection);

    Optional<Enrollment> findByStudentAndCourseSection(Student student, CourseSection courseSection);

    List<Enrollment> findByStatusAndCourseSection_AcademicTerm(EnrollmentStatus status, AcademicTerm term);

    boolean existsByStudentAndCourseSection(Student student, CourseSection courseSection);

    List<Enrollment> findAllByStudent(Student student);


    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student WHERE e.courseSection.id = :courseSectionId")
    List<Enrollment> findAllByCourseSectionId(Long courseSectionId);

}

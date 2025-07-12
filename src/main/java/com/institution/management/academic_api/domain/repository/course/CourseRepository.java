package com.institution.management.academic_api.domain.repository.course;

import com.institution.management.academic_api.application.dto.course.CourseStudentCountDto;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.course.Course;
import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    Page<Course> findByDepartment(Department department, Pageable pageable);

    Page<Course> findByDepartmentId(Long departmentId, Pageable pageable);

    Optional<Course> findByNameAndDepartment(String name, Department department);

    Page<Course> findByDepartment_Institution_IdAndNameContainingIgnoreCase(Long institutionId, String courseName, Pageable pageable);

    boolean existsByNameAndDepartment(String name, Department department);

    List<Course> findAllByDepartment(Department department);

    @Query("SELECT new com.institution.management.academic_api.application.dto.course.CourseStudentCountDto(c.name, COUNT(DISTINCT e.student.id)) " +
            "FROM Course c " +
            "JOIN c.subjects s " +
            "JOIN s.courseSections cs " +
            "JOIN cs.enrollments e " +
            "GROUP BY c.id, c.name " +
            "HAVING COUNT(DISTINCT e.student.id) > 0 " +
            "ORDER BY COUNT(DISTINCT e.student.id) DESC")
    List<CourseStudentCountDto> getStudentCountPerCourse();

    @Query("SELECT COUNT(DISTINCT cs.subject.course) FROM CourseSection cs WHERE cs.academicTerm.status IN :statuses")
    long countCoursesByTermStatusIn(@Param("statuses") List<AcademicTermStatus> statuses);
}

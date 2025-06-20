package com.institution.management.academic_api.domain.repository.course;

import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.course.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByDepartment(Department department, Pageable pageable);

    Page<Course> findByDepartmentId(Long departmentId, Pageable pageable);

    Optional<Course> findByNameAndDepartment(String name, Department department);

    Page<Course> findByDepartment_Institution_IdAndNameContainingIgnoreCase(Long institutionId, String courseName, Pageable pageable);
}

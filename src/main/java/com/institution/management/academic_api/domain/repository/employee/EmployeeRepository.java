package com.institution.management.academic_api.domain.repository.employee;

import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsEmployeeByEmail(String email);

    Page<Employee> findAll(Pageable pageable);

    Page<Employee> findByInstitutionId(Long institutionId, Pageable pageable);

    Optional<Employee> findByEmail(String email);

    List<Employee> findByDepartmentId(Long departmentId);

    List<Employee> findByDepartmentAndJobPositionIn(Department department, List<JobPosition> positions);

    List<Employee> findByDepartmentName(String name);

    long countByHiringDateAfter(LocalDate date);

}

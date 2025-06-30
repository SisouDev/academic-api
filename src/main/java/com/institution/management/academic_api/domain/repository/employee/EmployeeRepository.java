package com.institution.management.academic_api.domain.repository.employee;

import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsEmployeeByEmail(String email);

    Page<Employee> findAll(Pageable pageable);

    Page<Employee> findByInstitutionId(Long institutionId, Pageable pageable);
}

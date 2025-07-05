package com.institution.management.academic_api.domain.repository.academic;

import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    Optional<Department> findByAcronymIgnoreCase(String acronym);

    List<Department> findAllByInstitution(Institution institution);

    Optional<Department> findByNameAndInstitution(String name, Institution institution);

    boolean existsByNameAndInstitution(String name, Institution institution);

}

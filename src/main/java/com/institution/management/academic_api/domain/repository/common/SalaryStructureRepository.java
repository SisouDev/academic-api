package com.institution.management.academic_api.domain.repository.common;

import com.institution.management.academic_api.domain.model.entities.common.SalaryStructure;
import com.institution.management.academic_api.domain.model.enums.common.SalaryLevel;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, Long> {
    Optional<SalaryStructure> findByJobPositionAndLevel(JobPosition jobPosition, SalaryLevel salaryLevel);
}
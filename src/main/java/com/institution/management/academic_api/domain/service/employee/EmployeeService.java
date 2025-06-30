package com.institution.management.academic_api.domain.service.employee;

import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
import com.institution.management.academic_api.application.dto.employee.CreateEmployeeRequestDto;
import com.institution.management.academic_api.application.dto.employee.EmployeeResponseDto;
import com.institution.management.academic_api.application.dto.employee.EmployeeSummaryDto;
import com.institution.management.academic_api.application.dto.employee.UpdateEmployeeRequestDto;
import com.institution.management.academic_api.application.dto.institution.CreateInstitutionAdminRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionAdminResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeResponseDto createEmployee(CreateEmployeeRequestDto request);

    InstitutionAdminResponseDto createAdmin(CreateInstitutionAdminRequestDto request);

    PersonResponseDto findById(Long id);


    EmployeeResponseDto update(Long id, UpdateEmployeeRequestDto request);

    void delete(Long id);

    Page<EmployeeSummaryDto> findPaginated(Long institutionId, Pageable pageable);
}
package com.institution.management.academic_api.domain.service.employee;
import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.employee.CreateEmployeeRequestDto;
import com.institution.management.academic_api.application.dto.employee.EmployeeResponseDto;
import com.institution.management.academic_api.application.dto.employee.UpdateEmployeeRequestDto;
import com.institution.management.academic_api.application.dto.institution.CreateInstitutionAdminRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionAdminResponseDto;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDto createEmployee(CreateEmployeeRequestDto request);

    InstitutionAdminResponseDto createAdmin(CreateInstitutionAdminRequestDto request);

    PersonResponseDto findById(Long id);

    List<PersonSummaryDto> findAllByInstitution(Long institutionId);

    EmployeeResponseDto update(Long id, UpdateEmployeeRequestDto request);

    void delete(Long id);
}
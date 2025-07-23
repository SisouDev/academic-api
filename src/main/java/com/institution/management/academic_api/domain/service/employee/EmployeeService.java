package com.institution.management.academic_api.domain.service.employee;

import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
import com.institution.management.academic_api.application.dto.employee.*;
import com.institution.management.academic_api.application.dto.institution.CreateInstitutionAdminRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionAdminResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDto createEmployee(CreateEmployeeRequestDto request);

    InstitutionAdminResponseDto createAdmin(CreateInstitutionAdminRequestDto request);

    PersonResponseDto findById(Long id);


    EmployeeResponseDto update(Long id, UpdateEmployeeRequestDto request);

    void delete(Long id);

    Page<EmployeeSummaryDto> findPaginated(Long institutionId, Pageable pageable);

    Page<EmployeeListDto> findPaginatedSearchVersion(Long institutionId, String searchTerm, Pageable pageable);

    List<StaffListDto> findAllStaff(String searchTerm);
}
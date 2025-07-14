package com.institution.management.academic_api.domain.service.academic;

import com.institution.management.academic_api.application.dto.academic.DepartmentDetailsDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentRequestDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateDepartmentRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {

    DepartmentDetailsDto create(DepartmentRequestDto request);

    DepartmentDetailsDto findById(Long id);

    List<DepartmentSummaryDto> findAllByInstitution(Long institutionId);

    DepartmentDetailsDto update(Long id, UpdateDepartmentRequestDto request);

    void delete(Long id);

    List<DepartmentSummaryDto> findAllForCurrentUser();

    Page<DepartmentSummaryDto> findAll(String searchTerm, Pageable pageable);

    List<DepartmentSummaryDto> findAllForSelection();

}

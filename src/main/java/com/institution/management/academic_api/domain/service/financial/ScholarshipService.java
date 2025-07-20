package com.institution.management.academic_api.domain.service.financial;

import com.institution.management.academic_api.application.dto.financial.CreateScholarshipRequestDto;
import com.institution.management.academic_api.application.dto.financial.ScholarshipDetailsDto;
import com.institution.management.academic_api.domain.model.enums.financial.ScholarshipStatus;

import java.util.List;

public interface ScholarshipService {
    ScholarshipDetailsDto create(CreateScholarshipRequestDto dto);
    ScholarshipDetailsDto findById(Long id);
    List<ScholarshipDetailsDto> findByEnrollment(Long enrollmentId);
    void updateStatus(Long id, ScholarshipStatus status);
}
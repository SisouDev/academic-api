package com.institution.management.academic_api.domain.service.academic;

import com.institution.management.academic_api.application.dto.academic.AcademicTermDetailsDto;
import com.institution.management.academic_api.application.dto.academic.AcademicTermRequestDto;
import com.institution.management.academic_api.application.dto.academic.AcademicTermSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateAcademicTermRequestDto;

import java.util.List;

public interface AcademicTermService {
    AcademicTermDetailsDto create(AcademicTermRequestDto request);

    AcademicTermDetailsDto findById(Long id);

    List<AcademicTermSummaryDto> findAllByInstitution(Long institutionId);

    AcademicTermDetailsDto update(Long id, UpdateAcademicTermRequestDto request);

    void delete(Long id);
}
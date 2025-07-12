package com.institution.management.academic_api.domain.service.academic;

import com.institution.management.academic_api.application.dto.academic.AcademicTermDetailsDto;
import com.institution.management.academic_api.application.dto.academic.AcademicTermRequestDto;
import com.institution.management.academic_api.application.dto.academic.AcademicTermSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateAcademicTermRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AcademicTermService {
    AcademicTermDetailsDto create(AcademicTermRequestDto request);

    AcademicTermDetailsDto findById(Long id);

    AcademicTermDetailsDto update(Long id, UpdateAcademicTermRequestDto request);

    Page<AcademicTermSummaryDto> findPaginated(Long institutionId, Pageable pageable);


    void delete(Long id);
}
package com.institution.management.academic_api.domain.service.institution;
import com.institution.management.academic_api.application.dto.institution.CreateInstitutionRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionDetailsDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionSummaryDto;
import com.institution.management.academic_api.application.dto.institution.UpdateInstitutionRequestDto;

import java.util.List;

public interface InstitutionService {
    InstitutionDetailsDto create(CreateInstitutionRequestDto request);

    InstitutionDetailsDto findById(Long id);

    List<InstitutionSummaryDto> findAll();

    InstitutionDetailsDto update(Long id, UpdateInstitutionRequestDto request);

    void delete(Long id);
}
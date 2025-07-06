package com.institution.management.academic_api.domain.service.institution;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import com.institution.management.academic_api.application.dto.institution.CreateInstitutionRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionDetailsDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionSummaryDto;
import com.institution.management.academic_api.application.dto.institution.UpdateInstitutionRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InstitutionService {
    InstitutionDetailsDto create(CreateInstitutionRequestDto request);

    InstitutionDetailsDto findById(Long id);

    List<InstitutionSummaryDto> findAll();

    InstitutionDetailsDto update(Long id, UpdateInstitutionRequestDto request);

    void delete(Long id);
    Page<InstitutionSummaryDto> findPaginated(Pageable pageable);

    List<PersonSummaryDto> findTeachersByInstitution(Long id);

    List<CourseSectionSummaryDto> findCourseSectionsByInstitution(Long id);

    List<PersonSummaryDto> findStudentsByInstitution(Long id);
}
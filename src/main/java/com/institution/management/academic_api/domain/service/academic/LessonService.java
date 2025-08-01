package com.institution.management.academic_api.domain.service.academic;

import com.institution.management.academic_api.application.dto.academic.CreateLessonRequestDto;
import com.institution.management.academic_api.application.dto.academic.LessonDetailsDto;
import com.institution.management.academic_api.application.dto.academic.LessonSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateLessonRequestDto;

import java.util.List;

public interface LessonService {

    LessonDetailsDto create(CreateLessonRequestDto request);

    LessonDetailsDto findById(Long id);

    LessonDetailsDto update(Long id, UpdateLessonRequestDto request);

    void delete(Long id);

    List<LessonSummaryDto> findAllBySection(Long sectionId);
}
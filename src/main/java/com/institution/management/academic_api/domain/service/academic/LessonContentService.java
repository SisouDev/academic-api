package com.institution.management.academic_api.domain.service.academic;

import com.institution.management.academic_api.application.dto.academic.CreateLessonContentRequestDto;
import com.institution.management.academic_api.application.dto.academic.LessonContentDto;
import com.institution.management.academic_api.application.dto.academic.UpdateLessonContentRequestDto;

public interface LessonContentService {

    LessonContentDto create(CreateLessonContentRequestDto request);

    LessonContentDto update(Long id, UpdateLessonContentRequestDto request);

    void delete(Long id);
}
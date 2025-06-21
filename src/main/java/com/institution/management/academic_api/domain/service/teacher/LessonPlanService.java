package com.institution.management.academic_api.domain.service.teacher;

import com.institution.management.academic_api.application.dto.teacher.CreateLessonPlanRequestDto;
import com.institution.management.academic_api.application.dto.teacher.LessonPlanDto;
import com.institution.management.academic_api.application.dto.teacher.UpdateLessonPlanRequestDto;

public interface LessonPlanService {
    LessonPlanDto create(CreateLessonPlanRequestDto request);

    LessonPlanDto update(Long lessonPlanId, UpdateLessonPlanRequestDto request);

    LessonPlanDto findByCourseSection(Long courseSectionId);
    void delete(Long lessonPlanId);
}

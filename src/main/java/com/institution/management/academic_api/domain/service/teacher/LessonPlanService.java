package com.institution.management.academic_api.domain.service.teacher;

import com.institution.management.academic_api.application.dto.teacher.CreateLessonPlanRequestDto;
import com.institution.management.academic_api.application.dto.teacher.LessonPlanDto;

public interface LessonPlanService {
    LessonPlanDto createOrUpdate(CreateLessonPlanRequestDto request);
    LessonPlanDto findByCourseSection(Long courseSectionId);
    void delete(Long lessonPlanId);
}

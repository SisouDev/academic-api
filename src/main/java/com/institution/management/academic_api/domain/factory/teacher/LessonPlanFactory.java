package com.institution.management.academic_api.domain.factory.teacher;
import com.institution.management.academic_api.application.dto.teacher.CreateLessonPlanRequestDto;
import com.institution.management.academic_api.application.mapper.simple.teacher.LessonPlanMapper;
import com.institution.management.academic_api.domain.model.entities.teacher.LessonPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LessonPlanFactory {
    private final LessonPlanMapper lessonPlanMapper;

    public LessonPlan create(CreateLessonPlanRequestDto requestDto) {
        return lessonPlanMapper.toEntity(requestDto);
    }
}
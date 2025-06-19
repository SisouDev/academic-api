package com.institution.management.academic_api.application.mapper.teacher;

import com.institution.management.academic_api.application.dto.teacher.CreateLessonPlanRequestDto;
import com.institution.management.academic_api.application.dto.teacher.LessonPlanDto;
import com.institution.management.academic_api.application.dto.teacher.UpdateLessonPlanRequestDto;
import com.institution.management.academic_api.application.mapper.course.CourseSectionMapper;
import com.institution.management.academic_api.domain.model.entities.teacher.LessonPlan;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CourseSectionMapper.class})
public interface LessonPlanMapper {
    LessonPlanDto toDto(LessonPlan lessonPlan);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courseSection", ignore = true)
    LessonPlan toEntity(CreateLessonPlanRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courseSection", ignore = true)
    void updateFromDto(UpdateLessonPlanRequestDto dto, @MappingTarget LessonPlan entity);
}
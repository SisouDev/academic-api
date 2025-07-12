package com.institution.management.academic_api.application.mapper.simple.academic;

import com.institution.management.academic_api.application.dto.academic.CreateLessonContentRequestDto;
import com.institution.management.academic_api.application.dto.academic.LessonContentDto;
import com.institution.management.academic_api.domain.model.entities.academic.LessonContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonContentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "type", ignore = true)
    LessonContent toEntity(CreateLessonContentRequestDto dto);

    @Mapping(source = "type", target = "type")
    LessonContentDto toDto(LessonContent lessonContent);
}
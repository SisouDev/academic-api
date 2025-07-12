package com.institution.management.academic_api.application.mapper.simple.academic;

import com.institution.management.academic_api.application.dto.academic.CreateLessonRequestDto;
import com.institution.management.academic_api.application.dto.academic.LessonDetailsDto;
import com.institution.management.academic_api.domain.model.entities.academic.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LessonContentMapper.class})
public interface LessonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courseSection", ignore = true)
    @Mapping(target = "contents", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Lesson toEntity(CreateLessonRequestDto dto);

    LessonDetailsDto toDetailsDto(Lesson lesson);
}
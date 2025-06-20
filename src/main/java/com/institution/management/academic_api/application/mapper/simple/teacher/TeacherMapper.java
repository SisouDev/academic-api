package com.institution.management.academic_api.application.mapper.simple.teacher;

import com.institution.management.academic_api.application.dto.teacher.CreateTeacherRequestDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherResponseDto;
import com.institution.management.academic_api.application.dto.teacher.UpdateTeacherRequestDto;
import com.institution.management.academic_api.application.mapper.simple.course.CourseSectionMapper;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CourseSectionMapper.class})
public interface TeacherMapper {

    TeacherResponseDto toResponseDto(Teacher teacher);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "courseSections", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "document.type", source = "document.type")
    @Mapping(target = "document.number", source = "document.number")
    Teacher toEntity(CreateTeacherRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "document", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "courseSections", ignore = true)
    void updateFromDto(UpdateTeacherRequestDto dto, @MappingTarget Teacher entity);

}
package com.institution.management.academic_api.application.mapper.simple.course;

import com.institution.management.academic_api.application.dto.common.SubjectSearchResultDto;
import com.institution.management.academic_api.application.dto.course.CreateSubjectRequestDto;
import com.institution.management.academic_api.application.dto.course.SubjectDetailsDto;
import com.institution.management.academic_api.application.dto.course.SubjectSummaryDto;
import com.institution.management.academic_api.application.dto.course.UpdateSubjectRequestDto;
import com.institution.management.academic_api.application.mapper.wrappers.course.CourseSectionMapperWrapper;
import com.institution.management.academic_api.domain.model.entities.course.Subject;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {CourseMapper.class, CourseSectionMapperWrapper.class})
public interface SubjectMapper {

    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class);

    SubjectSummaryDto toSummaryDto(Subject subject);

    SubjectDetailsDto toDetailsDto(Subject subject);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courseSections", ignore = true)
    @Mapping(target = "course", ignore = true)
    Subject toEntity(CreateSubjectRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "courseSections", ignore = true)
    void updateFromDto(UpdateSubjectRequestDto dto, @MappingTarget Subject entity);

    @Mapping(target = "courseName", source = "course.name")
    SubjectSearchResultDto toSearchResultDto(Subject subject);
}
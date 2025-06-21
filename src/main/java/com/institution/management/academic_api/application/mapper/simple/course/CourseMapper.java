package com.institution.management.academic_api.application.mapper.simple.course;

import com.institution.management.academic_api.application.dto.course.CourseDetailsDto;
import com.institution.management.academic_api.application.dto.course.CourseSummaryDto;
import com.institution.management.academic_api.application.dto.course.CreateCourseRequestDto;
import com.institution.management.academic_api.application.dto.course.UpdateCourseRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.DepartmentMapper;
import com.institution.management.academic_api.application.mapper.wrappers.course.SubjectMapperWrapper;
import com.institution.management.academic_api.domain.model.entities.course.Course;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, SubjectMapperWrapper.class})
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseSummaryDto toSummaryDto(Course course);

    CourseDetailsDto toDetailsDto(Course course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    @Mapping(target = "department", ignore = true)
    Course toEntity(CreateCourseRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    void updateFromDto(UpdateCourseRequestDto dto, @MappingTarget Course entity);
}
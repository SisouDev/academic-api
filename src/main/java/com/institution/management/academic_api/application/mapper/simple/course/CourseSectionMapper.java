package com.institution.management.academic_api.application.mapper.simple.course;

import com.institution.management.academic_api.application.dto.course.CourseSectionDetailsDto;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import com.institution.management.academic_api.application.dto.course.CreateCourseSectionRequestDto;
import com.institution.management.academic_api.application.dto.course.UpdateCourseSectionRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.AcademicTermMapper;
import com.institution.management.academic_api.application.mapper.simple.teacher.TeacherMapper;
import com.institution.management.academic_api.application.mapper.wrappers.academic.AcademicTermMapperWrapper;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {
        SubjectMapper.class,
        AcademicTermMapperWrapper.class,
        TeacherMapper.class
})
public interface CourseSectionMapper {

    CourseSectionMapper INSTANCE = Mappers.getMapper(CourseSectionMapper.class);

    CourseSectionSummaryDto toSummaryDto(CourseSection courseSection);

    @Mapping(
            target = "enrolledStudentsCount",
            expression = "java(courseSection.getEnrollments() != null ? courseSection.getEnrollments().size() : 0)"
    )
    CourseSectionDetailsDto toDetailsDto(CourseSection courseSection);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "academicTerm", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    CourseSection toEntity(CreateCourseSectionRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "academicTerm", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    void updateFromDto(UpdateCourseSectionRequestDto dto, @MappingTarget CourseSection entity);
}
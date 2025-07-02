package com.institution.management.academic_api.application.mapper.simple.course;

import com.institution.management.academic_api.application.dto.course.*;
import com.institution.management.academic_api.application.mapper.simple.academic.DepartmentMapper;
import com.institution.management.academic_api.application.mapper.wrappers.course.SubjectMapperWrapper;
import com.institution.management.academic_api.domain.model.entities.course.Course;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.course.Subject;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, SubjectMapperWrapper.class})
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseSummaryDto toSummaryDto(Course course);

    @Mapping(target = "subjects", source = "subjects", qualifiedByName = "mapSubjectsToDetails")
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

    @Named("mapSubjectsToDetails")
    default List<SubjectDetailsDto> mapSubjectsToDetails(List<Subject> subjects) {
        if (subjects == null) return Collections.emptyList();

        CourseSectionMapper sectionMapper = Mappers.getMapper(CourseSectionMapper.class);

        return subjects.stream().map(subject -> {

            Optional<Teacher> teacherOpt = subject.getCourseSections().stream()
                    .findFirst()
                    .map(CourseSection::getTeacher);

            Long teacherId = teacherOpt.map(Teacher::getId).orElse(null);
            String teacherName = teacherOpt.map(teacher -> teacher.getFirstName() + " " + teacher.getLastName())
                    .orElse("A definir");

            CourseSummaryDto courseSummary = toSummaryDto(subject.getCourse());
            List<CourseSectionSummaryDto> sectionSummaries = subject.getCourseSections().stream()
                    .map(sectionMapper::toSummaryDto)
                    .collect(Collectors.toList());

            return new SubjectDetailsDto(
                    subject.getId(),
                    subject.getName(),
                    subject.getWorkloadHours(),
                    courseSummary,
                    sectionSummaries,
                    teacherId,
                    teacherName
            );
        }).collect(Collectors.toList());
    }
}
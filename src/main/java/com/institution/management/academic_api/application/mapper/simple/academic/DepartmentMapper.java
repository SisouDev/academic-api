package com.institution.management.academic_api.application.mapper.simple.academic;

import com.institution.management.academic_api.application.dto.academic.DepartmentDetailsDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentRequestDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateDepartmentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.course.CourseMapper;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.course.Course;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CourseMapper.class})
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    @Mapping(target = "courses", source = "courses")
    @Mapping(target = "courseCount", expression = "java(department.getCourses() != null ? department.getCourses().size() : 0)")
    @Mapping(target = "teacherCount", source = "courses", qualifiedByName = "countUniqueTeachersFromCourses")
    @Mapping(target = "studentCount", source = "courses", qualifiedByName = "countUniqueStudentsFromCourses")
    DepartmentDetailsDto toDetailsDto(Department department);

    DepartmentSummaryDto toSummaryDto(Department department);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "institution", ignore = true)
    Department toEntity(DepartmentRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "courses", ignore = true)
    void updateFromDto(UpdateDepartmentRequestDto dto, @MappingTarget Department entity);

    @Named("countUniqueTeachersFromCourses")
    default long countUniqueTeachersFromCourses(List<Course> courses) {
        if (courses == null) return 0;
        return courses.stream()
                .flatMap(course -> course.getSubjects().stream())
                .flatMap(subject -> subject.getCourseSections().stream())
                .map(CourseSection::getTeacher)
                .distinct()
                .count();
    }

    @Named("countUniqueStudentsFromCourses")
    default long countUniqueStudentsFromCourses(List<Course> courses) {
        if (courses == null) return 0;
        return courses.stream()
                .flatMap(course -> course.getSubjects().stream())
                .flatMap(subject -> subject.getCourseSections().stream())
                .flatMap(section -> section.getEnrollments().stream())
                .map(Enrollment::getStudent)
                .distinct()
                .count();
    }
}

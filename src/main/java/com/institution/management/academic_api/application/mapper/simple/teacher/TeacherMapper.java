package com.institution.management.academic_api.application.mapper.simple.teacher;

import com.institution.management.academic_api.application.dto.employee.StaffListDto;
import com.institution.management.academic_api.application.dto.teacher.*;
import com.institution.management.academic_api.application.mapper.simple.course.CourseSectionMapper;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CourseSectionMapper.class})
public interface TeacherMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "status", source = "status.displayName")
    @Mapping(target = "academicBackground", source = "academicBackground.displayName")
    @Mapping(target = "courseSections", source = "courseSections")
    @Mapping(target = "taughtSubjects", source = "courseSections", qualifiedByName = "mapToTaughtSubjects")
    @Mapping(target = "totalActiveSections", source = "courseSections", qualifiedByName = "countActiveSections")
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

    @Mapping(target = "academicBackground", source = "academicBackground.displayName")
    @Mapping(target = "status", source = "status.displayName")
    TeacherSummaryDto toSummaryDto(Teacher teacher);

    @Mapping(target = "fullName", expression = "java(teacher.getFirstName() + \" \" + teacher.getLastName())")
    @Mapping(target = "positionOrDegree", source = "academicBackground.displayName")
    @Mapping(target = "hiringDate", ignore = true)
    @Mapping(target = "baseSalary", source = "salaryStructure.baseSalary")
    StaffListDto toStaffListDto(Teacher teacher);

    @Named("mapToTaughtSubjects")
    default List<TaughtSubjectDto> mapToTaughtSubjects(List<CourseSection> courseSections) {
        if (courseSections == null) {
            return Collections.emptyList();
        }
        return courseSections.stream()
                .map(CourseSection::getSubject)
                .distinct()
                .map(subject -> new TaughtSubjectDto(
                        subject.getName(),
                        subject.getCourse().getName()
                ))
                .collect(Collectors.toList());
    }

    @Named("countActiveSections")
    default int countActiveSections(List<CourseSection> courseSections) {
        if (courseSections == null) {
            return 0;
        }
        return (int) courseSections.stream()
                .filter(section ->
                        section != null && section.getAcademicTerm() != null
                                && section.getAcademicTerm().getStatus() == AcademicTermStatus.IN_PROGRESS
                )
                .count();
    }
}
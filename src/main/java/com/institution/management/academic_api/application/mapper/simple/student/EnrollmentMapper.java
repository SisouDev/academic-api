package com.institution.management.academic_api.application.mapper.simple.student;

import com.institution.management.academic_api.application.dto.student.*;
import com.institution.management.academic_api.application.mapper.simple.course.CourseSectionMapper;
import com.institution.management.academic_api.application.mapper.wrappers.common.PersonMapperWrapper;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {
        PersonMapperWrapper.class,
        CourseSectionMapper.class,
        AssessmentMapper.class,
        AttendanceRecordMapper.class
})
public interface EnrollmentMapper {

    @Mapping(target = "status", source = "status.displayName")
    @Mapping(target = "courseSection", source = "courseSection")
    @Mapping(target = "teacher", source = "courseSection.teacher", qualifiedByName = "mapTeacherToEnrolledInfo")
    @Mapping(target = "courseSectionInfo", source = "courseSection")
    EnrollmentSummaryDto toSummaryDto(Enrollment enrollment);

    @Mapping(target = "status", source = "status.displayName")
    @Mapping(target = "student", source = "student")
    @Mapping(target = "courseSection", source = "courseSection")
    @Mapping(target = "assessments", source = "assessments")
    @Mapping(target = "attendanceRecords", source = "attendanceRecords")
    EnrollmentDetailsDto toDetailsDto(Enrollment enrollment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalAbsences", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "courseSection", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "assessments", ignore = true)
    @Mapping(target = "attendanceRecords", ignore = true)
    Enrollment toEntity(CreateEnrollmentRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrollmentDate", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "courseSection", ignore = true)
    @Mapping(target = "assessments", ignore = true)
    @Mapping(target = "attendanceRecords", ignore = true)
    void updateFromDto(UpdateEnrollmentRequestDto dto, @MappingTarget Enrollment entity);

    @Mapping(target = "enrollmentId", source = "id")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentName", expression = "java(enrollment.getStudent().getFirstName() + \" \" + enrollment.getStudent().getLastName())")
    @Mapping(target = "studentEmail", source = "student.email")
    @Mapping(target = "status", source = "status.displayName")
    ClassListStudentDto toClassListDto(Enrollment enrollment);

    @Named("mapTeacherToEnrolledInfo")
    default EnrolledTeacherInfo mapTeacherToEnrolledInfo(Teacher teacher) {
        if (teacher == null) {
            return new EnrolledTeacherInfo(null, "A definir");
        }
        return new EnrolledTeacherInfo(
                teacher.getId(),
                teacher.getFirstName() + " " + teacher.getLastName()
        );
    }
}
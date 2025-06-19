package com.institution.management.academic_api.application.mapper.student;

import com.institution.management.academic_api.application.dto.student.CreateEnrollmentRequestDto;
import com.institution.management.academic_api.application.dto.student.EnrollmentDetailsDto;
import com.institution.management.academic_api.application.dto.student.EnrollmentSummaryDto;
import com.institution.management.academic_api.application.dto.student.UpdateEnrollmentRequestDto;
import com.institution.management.academic_api.application.mapper.common.PersonMapper;
import com.institution.management.academic_api.application.mapper.course.CourseSectionMapper;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", uses = {
        PersonMapper.class,
        CourseSectionMapper.class,
        AssessmentMapper.class,
        AttendanceRecordMapper.class
})
public interface EnrollmentMapper {

    EnrollmentMapper INSTANCE = Mappers.getMapper(EnrollmentMapper.class);

    EnrollmentSummaryDto toSummaryDto(Enrollment enrollment);

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
}
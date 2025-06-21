package com.institution.management.academic_api.application.mapper.simple.student;

import com.institution.management.academic_api.application.dto.student.AttendanceRecordDto;
import com.institution.management.academic_api.application.dto.student.CreateAttendanceRecordRequestDto;
import com.institution.management.academic_api.application.dto.student.UpdateAttendanceRecordRequestDto;
import com.institution.management.academic_api.domain.model.entities.student.AttendanceRecord;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AttendanceRecordMapper {

    AttendanceRecordMapper INSTANCE = Mappers.getMapper(AttendanceRecordMapper.class);

    AttendanceRecordDto toDto(AttendanceRecord attendanceRecord);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrollment", ignore = true)
    AttendanceRecord toEntity(CreateAttendanceRecordRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrollment", ignore = true)
    void updateFromDto(UpdateAttendanceRecordRequestDto dto, @MappingTarget AttendanceRecord entity);
}
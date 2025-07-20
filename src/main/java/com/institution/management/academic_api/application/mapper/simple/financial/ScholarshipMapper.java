package com.institution.management.academic_api.application.mapper.simple.financial;

import com.institution.management.academic_api.application.dto.financial.ScholarshipDetailsDto;
import com.institution.management.academic_api.domain.model.entities.financial.Scholarship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScholarshipMapper {

    @Mapping(target = "enrollmentId", source = "enrollment.id")
    @Mapping(target = "studentName", source = "enrollment.student.firstName")
    @Mapping(target = "scholarshipName", source = "name")
    ScholarshipDetailsDto toDetailsDto(Scholarship scholarship);
}
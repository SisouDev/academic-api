package com.institution.management.academic_api.application.mapper.simple.employee;

import com.institution.management.academic_api.application.dto.employee.JobHistoryDto;
import com.institution.management.academic_api.domain.model.entities.employee.JobHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobHistoryMapper {

    @Mapping(target = "jobPosition", source = "jobPosition.displayName")
    @Mapping(target = "salaryLevel", source = "salaryStructure.level")
    @Mapping(target = "baseSalary", source = "salaryStructure.baseSalary")
    JobHistoryDto toDto(JobHistory jobHistory);
}
package com.institution.management.academic_api.application.mapper.simple.employee;

import com.institution.management.academic_api.application.dto.employee.*;
import com.institution.management.academic_api.application.mapper.simple.common.DocumentMapper;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class})
public interface EmployeeMapper {

    EmployeeResponseDto toDto(Employee employee);

    EmployeeDetailsDto toDetailsDto(Employee employee);

    @Mapping(target = "fullName", expression = "java(employee.getFirstName() + \" \" + employee.getLastName())")
    EmployeeSummaryDto toSummaryDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "jobPosition", ignore = true)
    Employee toEntity(CreateEmployeeRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "document", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFromDto(UpdateEmployeeRequestDto dto, @MappingTarget Employee entity);
}
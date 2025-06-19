package com.institution.management.academic_api.application.mapper.employee;

import com.institution.management.academic_api.application.dto.employee.CreateEmployeeRequestDto;
import com.institution.management.academic_api.application.dto.employee.EmployeeDetailsDto;
import com.institution.management.academic_api.application.dto.employee.EmployeeSummaryDto;
import com.institution.management.academic_api.application.dto.employee.UpdateEmployeeRequestDto;
import com.institution.management.academic_api.application.mapper.common.DocumentMapper;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class})
public interface EmployeeMapper {

    EmployeeDetailsDto toDetailsDto(Employee employee);

    @Mapping(target = "fullName", expression = "java(employee.getFirstName() + \" \" + employee.getLastName())")
    EmployeeSummaryDto toSummaryDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "institution", ignore = true)
    Employee toEntity(CreateEmployeeRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "document", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFromDto(UpdateEmployeeRequestDto dto, @MappingTarget Employee entity);
}
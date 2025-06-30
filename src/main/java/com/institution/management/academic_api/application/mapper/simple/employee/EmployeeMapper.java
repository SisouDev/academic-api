package com.institution.management.academic_api.application.mapper.simple.employee;

import com.institution.management.academic_api.application.dto.employee.*;
import com.institution.management.academic_api.application.mapper.simple.academic.DepartmentMapper;
import com.institution.management.academic_api.application.mapper.simple.common.DocumentMapper;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.Period;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class, DepartmentMapper.class})
public interface EmployeeMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "profilePictureUrl", source = "profilePictureUrl")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "status", source = "status.displayName")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "document", source = "document")
    @Mapping(target = "institution", source = "institution")
    @Mapping(target = "formattedAddress", ignore = true)
    @Mapping(target = "jobPosition", source = "jobPosition.displayName")
    @Mapping(target = "hiringDate", source = "hiringDate")
    EmployeeResponseDto toDto(Employee employee);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "profilePictureUrl", source = "profilePictureUrl")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "status", source = "status.displayName")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "document", source = "document")
    @Mapping(target = "institution", source = "institution")
    @Mapping(target = "formattedAddress", ignore = true)
    @Mapping(target = "jobPosition", source = "jobPosition.displayName")
    @Mapping(target = "hiringDate", source = "hiringDate")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "yearsOfService", source = "hiringDate", qualifiedByName = "calculateYearsOfService")
    EmployeeDetailsDto toDetailsDto(Employee employee);

    @Mapping(target = "fullName", expression = "java(employee.getFirstName() + \" \" + employee.getLastName())")
    @Mapping(target = "jobPosition", source = "jobPosition.displayName")
    EmployeeSummaryDto toSummaryDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "jobPosition", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "profilePictureUrl", ignore = true)
    Employee toEntity(CreateEmployeeRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "document", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "department", ignore = true)
    void updateFromDto(UpdateEmployeeRequestDto dto, @MappingTarget Employee entity);

    @Named("calculateYearsOfService")
    default long calculateYearsOfService(LocalDate hiringDate) {
        if (hiringDate == null) {
            return 0;
        }
        return Period.between(hiringDate, LocalDate.now()).getYears();
    }
}
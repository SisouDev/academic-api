package com.institution.management.academic_api.application.mapper.simple.common;

import com.institution.management.academic_api.application.dto.common.PayrollRecordDetailsDto;
import com.institution.management.academic_api.application.dto.common.PayrollRecordSummaryDto;
import com.institution.management.academic_api.domain.model.entities.common.PayrollRecord;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PayrollRecordMapper {
    @Mapping(target = "personId", source = "person.id")
    @Mapping(target = "personName", source = "person", qualifiedByName = "personToFullName")
    @Mapping(target = "personJobPosition", source = "person", qualifiedByName = "personToJobPosition")
    @Mapping(target = "createdAt", source = "createdAt")
    PayrollRecordDetailsDto toDetailsDto(PayrollRecord payrollRecord);

    @Mapping(target = "personName", source = "person", qualifiedByName = "personToFullName")
    @Mapping(target = "personJobPosition", source = "person", qualifiedByName = "personToJobPosition")
    PayrollRecordSummaryDto toSummaryDto(PayrollRecord payrollRecord);

    List<PayrollRecordSummaryDto> toSummaryDtoList(List<PayrollRecord> payrollRecords);



    @Named("personToFullName")
    default String personToFullName(Person person) {
        if (person == null) {
            return null;
        }
        return person.getFirstName() + " " + person.getLastName();
    }

    @Named("personToJobPosition")
    default String personToJobPosition(Person person) {
        if (person instanceof Employee employee) {
            if (employee.getJobPosition() != null) {
                return employee.getJobPosition().getDisplayName();
            }
        }
        return person.getPersonType().getDisplayName();
    }
}
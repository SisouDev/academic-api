package com.institution.management.academic_api.domain.factory.employee;
import com.institution.management.academic_api.application.dto.employee.CreateEmployeeRequestDto;
import com.institution.management.academic_api.application.mapper.employee.EmployeeMapper;
import com.institution.management.academic_api.domain.factory.common.PersonFactory;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeFactory implements PersonFactory {

    private final EmployeeMapper employeeMapper;

    @Override
    public Person create(Object requestDto) {
        if (!(requestDto instanceof CreateEmployeeRequestDto)) {
            throw new IllegalArgumentException("Invalid DTO for EmployeeFactory.");
        }
        return employeeMapper.toEntity((CreateEmployeeRequestDto) requestDto);
    }

    @Override
    public PersonType supportedType() {
        return PersonType.EMPLOYEE;
    }
}
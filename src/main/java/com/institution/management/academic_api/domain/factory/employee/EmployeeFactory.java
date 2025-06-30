package com.institution.management.academic_api.domain.factory.employee;

import com.institution.management.academic_api.application.dto.employee.CreateEmployeeRequestDto;
import com.institution.management.academic_api.application.mapper.simple.employee.EmployeeMapper;
import com.institution.management.academic_api.domain.factory.common.PersonFactory;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeFactory implements PersonFactory {

    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;

    @Override
    public Person create(Object requestDto) {
        if (!(requestDto instanceof CreateEmployeeRequestDto dto)) {
            throw new IllegalArgumentException("Invalid DTO for EmployeeFactory.");
        }
        Employee employee = employeeMapper.toEntity(dto);

        if (dto.departmentId() != null) {
            Department department = departmentRepository.findById(dto.departmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department with ID " + dto.departmentId() + " not found."));
            employee.setDepartment(department);
        }

        return employee;
    }

    @Override
    public PersonType supportedType() {
        return PersonType.EMPLOYEE;
    }
}
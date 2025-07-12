package com.institution.management.academic_api.application.service.common;

import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.common.NotificationAudienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationAudienceServiceImpl implements NotificationAudienceService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<User> getUsersForInstitutionalScope() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersForDepartmentScope(Long departmentId) {
        if (departmentId == null) {
            return List.of();
        }

        List<Employee> employeesInDepartment = employeeRepository.findByDepartmentId(departmentId);

        return employeesInDepartment.stream()
                .map(Employee::getUser)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
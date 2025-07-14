package com.institution.management.academic_api.application.service.utils;

import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class RoundRobinAssignerService {

    private final EmployeeRepository employeeRepository;
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    public Employee getNextHandlerByJobPosition(JobPosition position) {
        List<Employee> eligibleHandlers = employeeRepository.findByJobPosition(position);

        if (eligibleHandlers.isEmpty()) {
            return null;
        }

        int nextIndex = currentIndex.getAndIncrement() % eligibleHandlers.size();

        return eligibleHandlers.get(nextIndex);
    }
}
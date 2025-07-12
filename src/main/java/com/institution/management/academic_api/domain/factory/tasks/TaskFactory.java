package com.institution.management.academic_api.domain.factory.tasks;

import com.institution.management.academic_api.application.dto.tasks.CreateTaskRequestDto;
import com.institution.management.academic_api.application.mapper.simple.tasks.TaskMapper;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.tasks.Task;
import com.institution.management.academic_api.domain.model.enums.tasks.TaskStatus;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TaskFactory {

    private final TaskMapper taskMapper;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public Task create(CreateTaskRequestDto dto, Employee creator) {
        Task task = taskMapper.toEntity(dto);

        task.setCreatedAt(LocalDateTime.now());
        task.setStatus(TaskStatus.TODO);
        task.setCreatedBy(creator);

        Department department = departmentRepository.findById(dto.departmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + dto.departmentId()));
        task.setDepartment(department);

        if (dto.assigneeId() != null) {
            Employee assignee = employeeRepository.findById(dto.assigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee (Employee) not found with id: " + dto.assigneeId()));
            task.setAssignee(assignee);
        }

        return task;
    }
}
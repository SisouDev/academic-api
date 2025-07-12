package com.institution.management.academic_api.application.service.tasks;

import com.institution.management.academic_api.application.dto.tasks.CreateTaskRequestDto;
import com.institution.management.academic_api.application.dto.tasks.TaskDetailsDto;
import com.institution.management.academic_api.application.dto.tasks.UpdateTaskRequestDto;
import com.institution.management.academic_api.application.mapper.simple.tasks.TaskMapper;
import com.institution.management.academic_api.application.notifiers.tasks.TaskNotifier;
import com.institution.management.academic_api.domain.factory.tasks.TaskFactory;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.tasks.Task;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.tasks.TaskRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import com.institution.management.academic_api.domain.service.tasks.TaskService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final TaskFactory taskFactory;
    private final TaskMapper taskMapper;
    private final NotificationService notificationService;
    private final TaskNotifier taskNotifier;

    @Override
    @Transactional
    @LogActivity("Criou uma nova tarefa.")
    public TaskDetailsDto create(CreateTaskRequestDto dto, String creatorEmail) {
        Employee creator = employeeRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new EntityNotFoundException("Creator not found with email: " + creatorEmail));

        Task newTask = taskFactory.create(dto, creator);
        Task savedTask = taskRepository.save(newTask);

        if (savedTask.getAssignee() != null) {
            taskNotifier.notifyAssigneeOfNewTask(savedTask);
        }

        return taskMapper.toDetailsDto(savedTask);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou uma tarefa.")
    public TaskDetailsDto update(Long taskId, UpdateTaskRequestDto dto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

        Long oldAssigneeId = (task.getAssignee() != null) ? task.getAssignee().getId() : null;

        taskMapper.updateFromDto(dto, task);

        if (dto.assigneeId() != null && !Objects.equals(dto.assigneeId(), oldAssigneeId)) {
            Employee newAssignee = employeeRepository.findById(dto.assigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("New responsible not found with id: " + dto.assigneeId()));
            task.setAssignee(newAssignee);
            taskNotifier.notifyAssigneeOfNewTask(task);
        }

        return taskMapper.toDetailsDto(task);
    }

    @Override
    @Transactional
    @LogActivity("Deletou uma tarefa.")
    public void delete(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new EntityNotFoundException("Task not found with id: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDetailsDto findById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDetailsDto> findByDepartment(Long departmentId) {
        return taskRepository.findByDepartmentId(departmentId).stream()
                .map(taskMapper::toDetailsDto)
                .collect(Collectors.toList());
    }
}

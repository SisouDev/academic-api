package com.institution.management.academic_api.domain.service.tasks;

import com.institution.management.academic_api.application.dto.tasks.CreateTaskRequestDto;
import com.institution.management.academic_api.application.dto.tasks.TaskDetailsDto;
import com.institution.management.academic_api.application.dto.tasks.UpdateTaskRequestDto;

import java.util.List;

public interface TaskService {
    TaskDetailsDto create(CreateTaskRequestDto dto, String creatorEmail);

    TaskDetailsDto update(Long taskId, UpdateTaskRequestDto dto);

    void delete(Long taskId);

    TaskDetailsDto findById(Long id);

    List<TaskDetailsDto> findByDepartment(Long departmentId);
}

package com.institution.management.academic_api.application.mapper.simple.tasks;

import com.institution.management.academic_api.application.dto.tasks.CreateTaskRequestDto;
import com.institution.management.academic_api.application.dto.tasks.TaskDetailsDto;
import com.institution.management.academic_api.application.dto.tasks.TaskSummaryDto;
import com.institution.management.academic_api.application.dto.tasks.UpdateTaskRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.DepartmentMapper;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.tasks.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        PersonMapper.class,
        DepartmentMapper.class
})
public interface TaskMapper {
    @Mapping(source = "assignee", target = "assignee")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "department", target = "department")
    TaskDetailsDto toDetailsDto(Task task);

    @Mapping(target = "assigneeName", source = "assignee", qualifiedByName = "personToFullName")
    @Mapping(target = "departmentName", source = "department.name")
    TaskSummaryDto toSummaryDto(Task task);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Task toEntity(CreateTaskRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    void updateFromDto(UpdateTaskRequestDto dto, @MappingTarget Task entity);
}
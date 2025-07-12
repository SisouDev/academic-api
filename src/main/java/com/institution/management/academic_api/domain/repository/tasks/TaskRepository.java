package com.institution.management.academic_api.domain.repository.tasks;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.tasks.Task;
import com.institution.management.academic_api.domain.model.enums.tasks.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByDepartmentId(Long departmentId);

    List<Task> findByAssigneeId(Long assigneeId);

    List<Task> findByStatus(TaskStatus status);

    long countByAssigneeAndStatusNot(Person assignee, TaskStatus status);

    List<Task> findTop5ByAssigneeAndStatusNotOrderByDueDateAsc(Person assignee, TaskStatus status);

}

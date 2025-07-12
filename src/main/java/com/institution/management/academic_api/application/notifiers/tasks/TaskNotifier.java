package com.institution.management.academic_api.application.notifiers.tasks;

import com.institution.management.academic_api.domain.model.entities.tasks.Task;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskNotifier {

    private final NotificationService notificationService;

    public void notifyAssigneeOfNewTask(Task task) {
        if (task.getAssignee() == null || task.getAssignee().getUser() == null) {
            return;
        }

        String message = String.format("Uma nova tarefa foi atribuída a você: '%s'", task.getTitle());
        String link = "/tasks/my-tasks/" + task.getId();

        notificationService.createNotification(
                task.getAssignee().getUser(),
                message,
                link,
                NotificationType.TASK_ASSIGNED
        );
    }
}
package com.institution.management.academic_api.application.notifiers.request;

import com.institution.management.academic_api.domain.model.entities.request.InternalRequest;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.service.common.NotificationAudienceService;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InternalRequestNotifier {

    private final NotificationService notificationService;
    private final NotificationAudienceService audienceService;

    public void notifyDepartmentOfNewRequest(InternalRequest request) {
        if (request.getTargetDepartment() == null) return;

        List<User> departmentManagers = audienceService.getUsersForDepartmentScope(request.getTargetDepartment().getId());
        String message = String.format("Nova requisição interna: '%s'", request.getTitle());
        String link = "/admin/requests/" + request.getId();

        departmentManagers.forEach(manager -> {
            if (!manager.getPerson().getId().equals(request.getRequester().getId())) {
                notificationService.createNotification(manager, message, link, NotificationType.TASK_ASSIGNED);
            }
        });
    }

    public void notifyRequesterOfAssignment(InternalRequest request) {
        if (request.getHandler() == null) return;

        String message = String.format("Sua requisição '%s' está sendo atendida por %s.",
                request.getTitle(),
                request.getHandler().getFirstName());
        String link = "/my-requests/" + request.getId();

        notificationService.createNotification(request.getRequester().getUser(), message, link, NotificationType.UPDATE);
    }

    public void notifyRequesterOfStatusChange(InternalRequest request) {
        String message = String.format("O status da sua requisição '%s' foi atualizado para: %s",
                request.getTitle(),
                request.getStatus().getDisplayName());
        String link = "/my-requests/" + request.getId();

        notificationService.createNotification(request.getRequester().getUser(), message, link, NotificationType.GENERAL_INFO);
    }
}
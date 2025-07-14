package com.institution.management.academic_api.application.notifiers.helpDesk;

import com.institution.management.academic_api.domain.model.entities.helpDesk.SupportTicket;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import com.institution.management.academic_api.domain.service.common.NotificationAudienceService;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SupportTicketNotifier {

    private final NotificationService notificationService;
    private final NotificationAudienceService audienceService;
    private final DepartmentRepository departmentRepository;

    public void notifySupportTeamOfNewTicket(SupportTicket ticket) {
        departmentRepository.findByNameIgnoreCase("TI")
                .ifPresentOrElse(tiDepartment -> {

                            List<User> supportTeam = audienceService.getUsersForDepartmentScope(tiDepartment.getId());

                            String message = String.format("Novo chamado de suporte: #%d - %s", ticket.getId(), ticket.getTitle());
                            String link = "/admin/support/tickets/" + ticket.getId();

                            supportTeam.forEach(user ->
                                    notificationService.createNotification(
                                            user,
                                            message,
                                            link,
                                            NotificationType.TASK_ASSIGNED
                                    )
                            );
                            log.info("Notificando {} membros da equipe de TI sobre o novo ticket #{}", supportTeam.size(), ticket.getId());

                        }, () -> log.warn("Departamento 'TI' não encontrado. Não foi possível notificar a equipe de suporte sobre o ticket #{}.", ticket.getId())
                );
    }

    public void notifyAssigneeOfNewTicket(SupportTicket ticket) {
        if (ticket.getAssignee() == null || ticket.getAssignee().getUser() == null) return;

        String message = String.format(
                "Um novo chamado de suporte foi atribuído a você: #%d - %s",
                ticket.getId(),
                ticket.getTitle()
        );
        String link = "/support/tickets/" + ticket.getId();

        notificationService.createNotification(
                ticket.getAssignee().getUser(),
                message,
                link,
                NotificationType.TASK_ASSIGNED
        );
    }

    public void notifyRequesterOfAssignment(SupportTicket ticket) {
        if (ticket.getAssignee() == null) return;

        String message = String.format(
                "Seu chamado '%s' está sendo atendido por %s.",
                ticket.getTitle(),
                ticket.getAssignee().getFirstName()
        );
        String link = "/support/tickets/" + ticket.getId();

        notificationService.createNotification(
                ticket.getRequester().getUser(),
                message,
                link,
                NotificationType.UPDATE
        );
    }

    public void notifyRequesterOfResolution(SupportTicket ticket) {
        String message = String.format(
                "Seu chamado '%s' foi marcado como '%s'.",
                ticket.getTitle(),
                ticket.getStatus().getDisplayName()
        );
        String link = "/support/tickets/" + ticket.getId();

        notificationService.createNotification(
                ticket.getRequester().getUser(),
                message,
                link,
                NotificationType.SUCCESS
        );
    }
}
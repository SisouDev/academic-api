package com.institution.management.academic_api.application.notifiers.financial;

import com.institution.management.academic_api.domain.model.entities.financial.PurchaseRequest;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PurchaseRequestNotifier {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public void notifyFinanceAssistantsOfNewRequest(PurchaseRequest request) {
        List<User> assistants = userRepository.findByRoles_NameIn(List.of(RoleName.ROLE_FINANCE_ASSISTANT));

        String requesterName = request.getRequester().getFirstName() + " " + request.getRequester().getLastName();
        String message = String.format(
                "Nova requisição de compra para '%s' (qtde: %d) enviada por %s.",
                request.getItemName(),
                request.getQuantity(),
                requesterName
        );
        String link = "/finance/purchase-requests/" + request.getId();

        assistants.forEach(assistant ->
                notificationService.createNotification(
                        assistant,
                        message,
                        link,
                        NotificationType.ALERT
                )
        );
    }

    public void notifyRequesterOfReview(PurchaseRequest request) {
        User requesterUser = request.getRequester().getUser();
        if (requesterUser == null) {
            return;
        }

        String statusMessage;
        NotificationType notificationType;

        switch (request.getStatus()) {
            case APPROVED_BY_ASSISTANT:
                statusMessage = "APROVADA pelo assistente e encaminhada para aprovação final.";
                notificationType = NotificationType.SUCCESS;
                break;
            case REJECTED_BY_ASSISTANT:
                statusMessage = "REJEITADA pelo assistente.";
                notificationType = NotificationType.ALERT;
                break;
            default:
                return;
        }

        String message = String.format(
                "Sua requisição de compra para '%s' foi %s",
                request.getItemName(),
                statusMessage
        );
        String link = "/my-requests";

        notificationService.createNotification(
                requesterUser,
                message,
                link,
                notificationType
        );
    }
}
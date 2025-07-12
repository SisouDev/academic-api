package com.institution.management.academic_api.application.notifiers.it;

import com.institution.management.academic_api.domain.model.entities.it.Asset;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssetNotifier {

    private final NotificationService notificationService;

    public void notifyEmployeeOfAssignment(Asset asset) {
        if (asset == null || asset.getAssignedTo() == null || asset.getAssignedTo().getUser() == null) {
            return;
        }

        String message = String.format(
                "O equipamento '%s' (Patrimônio: %s) foi alocado para você.",
                asset.getName(),
                asset.getAssetTag()
        );
        String link = "/my-profile/assets/" + asset.getId();

        notificationService.createNotification(
                asset.getAssignedTo().getUser(),
                message,
                link,
                NotificationType.GENERAL_INFO
        );
    }
}
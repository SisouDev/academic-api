package com.institution.management.academic_api.application.notifiers.user;

import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserNotifier {

    private final NotificationService notificationService;

    public void notifyUserWelcome(User newUser) {
        String message = String.format("Bem-vindo(a) ao sistema, %s! Sua conta foi criada com sucesso.", newUser.getPerson().getFirstName());
        String link = "/my-profile";

        notificationService.createNotification(newUser, message, link, NotificationType.SUCCESS);
    }

    public void notifyPasswordChange(User user) {
        String message = "Sua senha foi alterada com sucesso. Se não foi você, entre em contato com o suporte.";
        String link = "/my-profile/security";

        notificationService.createNotification(user, message, link, NotificationType.SECURITY_ALERT);
    }

    public void notifyPasswordReset(User user) {
        String message = "A senha da sua conta foi resetada por um administrador. Verifique seu e-mail para mais instruções.";
        String link = "/login";

        notificationService.createNotification(user, message, link, NotificationType.SECURITY_ALERT);
    }
}
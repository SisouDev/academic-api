package com.institution.management.academic_api.application.notifiers.financial;

import com.institution.management.academic_api.domain.model.entities.financial.FinancialTransaction;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FinancialTransactionNotifier {

    private final NotificationService notificationService;

    public void notifyStudentOfNewTransaction(FinancialTransaction transaction) {
        if (transaction == null || transaction.getStudent() == null || transaction.getStudent().getUser() == null) {
            return;
        }
        String message = String.format(
                "Nova transação registrada: %s no valor de R$ %.2f",
                transaction.getDescription(),
                transaction.getAmount()
        );

        String link = "/financial/statement/" + transaction.getId();
        notificationService.createNotification(
                transaction.getStudent().getUser(),
                message,
                link,
                NotificationType.FINANCIAL
        );
    }
}
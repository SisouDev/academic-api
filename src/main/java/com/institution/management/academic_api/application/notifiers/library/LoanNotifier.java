package com.institution.management.academic_api.application.notifiers.library;

import com.institution.management.academic_api.domain.model.entities.library.Loan;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.model.enums.library.LoanStatus;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class LoanNotifier {

    private final NotificationService notificationService;

    public void notifyBorrowerOfNewLoan(Loan loan) {
        if (loan == null || loan.getBorrower() == null || loan.getBorrower().getUser() == null) {
            return;
        }

        String message = String.format(
                "Você pegou o item '%s' emprestado. Data de devolução: %s.",
                loan.getItem().getTitle(),
                loan.getDueDate().toString()
        );
        String link = "/my-profile/loans/" + loan.getId();

        notificationService.createNotification(
                loan.getBorrower().getUser(),
                message,
                link,
                NotificationType.GENERAL_INFO
        );
    }

    public void notifyBorrowerOfLateFee(Loan loan, BigDecimal fineAmount) {
        if (loan == null || loan.getBorrower() == null || loan.getBorrower().getUser() == null) {
            return;
        }

        String message = String.format(
                "Foi gerada uma multa de R$ %.2f por atraso na devolução do item '%s'.",
                fineAmount,
                loan.getItem().getTitle()
        );
        String link = "/financial/statement";

        notificationService.createNotification(
                loan.getBorrower().getUser(),
                message,
                link,
                NotificationType.WARNING
        );
    }

    public void notifyUserOfLoanStatusChange(Loan updatedLoan) {
        User userToNotify = updatedLoan.getBorrower().getUser();
        if (userToNotify == null) {
            return;
        }

        String statusMessage = getStatusMessage(updatedLoan.getStatus());
        String message = String.format(
                "O status do seu empréstimo para o item '%s' foi atualizado para: %s.",
                updatedLoan.getLibraryItem().getTitle(),
                statusMessage
        );
        String link = "/my-loans";

        notificationService.createNotification(
                userToNotify,
                message,
                link,
                NotificationType.LIBRARY_UPDATE
        );
    }

    private String getStatusMessage(LoanStatus status) {
        return switch (status) {
            case ACTIVE -> "Aprovado e Ativo";
            case REJECTED -> "Recusado";
            case RETURNED -> "Devolvido";
            case OVERDUE -> "Atrasado";
            case PENDING -> "Pendente";
            default -> status.name();
        };
    }
}
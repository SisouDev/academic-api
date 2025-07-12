package com.institution.management.academic_api.application.service.library;

import com.institution.management.academic_api.application.dto.financial.CreateFinancialTransactionRequestDto;
import com.institution.management.academic_api.application.dto.library.CreateLoanRequestDto;
import com.institution.management.academic_api.application.dto.library.LoanDetailsDto;
import com.institution.management.academic_api.application.mapper.simple.library.LoanMapper;
import com.institution.management.academic_api.application.notifiers.library.LoanNotifier;
import com.institution.management.academic_api.domain.factory.library.LoanFactory;
import com.institution.management.academic_api.domain.model.entities.library.LibraryItem;
import com.institution.management.academic_api.domain.model.entities.library.Loan;
import com.institution.management.academic_api.domain.model.enums.library.LoanStatus;
import com.institution.management.academic_api.domain.repository.library.LoanRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import com.institution.management.academic_api.domain.service.financial.FinancialTransactionService;
import com.institution.management.academic_api.domain.service.library.LoanService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final LoanFactory loanFactory;
    private final LoanMapper loanMapper;
    private final NotificationService notificationService;
    private final LoanNotifier loanNotifier;
    private final FinancialTransactionService financialTransactionService;

    @Override
    @Transactional
    @LogActivity("Registrou um novo empréstimo de item da biblioteca.")
    public LoanDetailsDto create(CreateLoanRequestDto dto) {
        Loan newLoan = loanFactory.create(dto);
        Loan savedLoan = loanRepository.save(newLoan);

        loanNotifier.notifyBorrowerOfNewLoan(savedLoan);

        return loanMapper.toDetailsDto(savedLoan);
    }

    @Override
    @Transactional
    @LogActivity("Registrou a devolução de um item da biblioteca.")
    public LoanDetailsDto returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found with id: " + loanId));

        if (loan.getStatus() != LoanStatus.ACTIVE && loan.getStatus() != LoanStatus.OVERDUE) {
            throw new IllegalStateException("This loan is not active and cannot be returned.");
        }

        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());

        LibraryItem item = loan.getItem();
        item.setAvailableCopies(item.getAvailableCopies() + 1);

        long daysOverdue = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());
        if (daysOverdue > 7) {
            BigDecimal fineAmount = new BigDecimal("1.00").multiply(new BigDecimal(daysOverdue - 7));

            CreateFinancialTransactionRequestDto fineDto = new CreateFinancialTransactionRequestDto(
                    loan.getBorrower().getId(),
                    "Multa por atraso na devolução: " + item.getTitle(),
                    fineAmount.negate(),
                    "LATE_FEE_DEBIT",
                    LocalDate.now()
            );

            financialTransactionService.create(fineDto);

            loanNotifier.notifyBorrowerOfLateFee(loan, fineAmount);
        }

        return loanMapper.toDetailsDto(loan);
    }

    @Override
    public LoanDetailsDto findById(Long id) {
        return loanRepository.findById(id)
                .map(loanMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found with id: " + id));
    }

    @Override
    public List<LoanDetailsDto> findByBorrower(Long borrowerId) {
        return loanRepository.findByBorrowerId(borrowerId).stream()
                .map(loanMapper::toDetailsDto)
                .collect(Collectors.toList());
    }
}

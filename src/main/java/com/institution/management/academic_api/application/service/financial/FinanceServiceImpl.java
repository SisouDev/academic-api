package com.institution.management.academic_api.application.service.financial;

import com.institution.management.academic_api.application.dto.dashboard.director.*;
import com.institution.management.academic_api.application.dto.financial.PayableSummaryDto;
import com.institution.management.academic_api.application.mapper.simple.financial.FinancialTransactionMapper;
import com.institution.management.academic_api.domain.model.entities.financial.FinancialTransaction;
import com.institution.management.academic_api.domain.model.enums.common.PayrollStatus;
import com.institution.management.academic_api.domain.model.enums.financial.OrderStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus;
import com.institution.management.academic_api.domain.repository.common.PayrollRecordRepository;
import com.institution.management.academic_api.domain.repository.financial.FinancialTransactionRepository;
import com.institution.management.academic_api.domain.repository.financial.PurchaseOrderRepository;
import com.institution.management.academic_api.domain.service.financial.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    private final PayrollRecordRepository payrollRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final FinancialTransactionRepository transactionRepository;
    private final FinancialTransactionMapper transactionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PayableSummaryDto> getPendingPayables() {
        Stream<PayableSummaryDto> pendingPayrolls = payrollRepository.findAllByStatus(PayrollStatus.PENDING).stream()
                .map(pr -> new PayableSummaryDto(
                        "payroll-" + pr.getId(),
                        "Folha de Pagamento",
                        "Salário: " + pr.getPerson().getFirstName() + " " + pr.getPerson().getLastName(),
                        pr.getNetPay(),
                        pr.getReferenceMonth().withDayOfMonth(pr.getReferenceMonth().lengthOfMonth()),
                        pr.getStatus().name()
                ));

        Stream<PayableSummaryDto> approvedPOs = purchaseOrderRepository.findAllByStatus(OrderStatus.APPROVED).stream()
                .map(po -> new PayableSummaryDto(
                        "po-" + po.getId(),
                        "Ordem de Compra",
                        po.getDescription(),
                        po.getAmount(),
                        po.getDueDate(),
                        po.getStatus().name()
                ));

        List<PayableSummaryDto> combinedList = new ArrayList<>(Stream.concat(pendingPayrolls, approvedPOs).toList());
        combinedList.sort(Comparator.comparing(PayableSummaryDto::dueDate));

        return combinedList;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDetailDto> findProblematicTransactions(Pageable pageable) {
        List<TransactionStatus> problematicStatuses = List.of(TransactionStatus.PENDING, TransactionStatus.FAILED, TransactionStatus.IN_DISPUTE);
        return transactionRepository
                .findByStatusIn(problematicStatuses, pageable)
                .map(transactionMapper::toDetailDto);
    }

    @Override
    @Transactional(readOnly = true)
    public DirectorFinancialReportDto getDirectorFinancialReport() {
        LocalDate today = LocalDate.now();
        LocalDate startOfYear = today.with(TemporalAdjusters.firstDayOfYear());

        BigDecimal totalRevenueYTD = transactionRepository.sumCompletedTransactions(startOfYear, today).orElse(BigDecimal.ZERO);
        BigDecimal payrollExpensesYTD = payrollRepository.sumPaidInDateRange(startOfYear, today).orElse(BigDecimal.ZERO);
        BigDecimal purchaseExpensesYTD = purchaseOrderRepository.sumApprovedOrdersInDateRange(startOfYear, today).orElse(BigDecimal.ZERO);
        BigDecimal totalExpensesYTD = payrollExpensesYTD.add(purchaseExpensesYTD);
        BigDecimal netIncomeYTD = totalRevenueYTD.subtract(totalExpensesYTD);
        BigDecimal profitMargin = (totalRevenueYTD.compareTo(BigDecimal.ZERO) > 0)
                ? netIncomeYTD.divide(totalRevenueYTD, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                : BigDecimal.ZERO;

        List<FinancialTransaction> problematicTxs = transactionRepository.findAllProblematicTransactions();
        BigDecimal totalAccountsReceivable = problematicTxs.stream()
                .map(FinancialTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal overdue30 = BigDecimal.ZERO;
        BigDecimal overdue60 = BigDecimal.ZERO;
        BigDecimal overdue90 = BigDecimal.ZERO;
        BigDecimal overdueOver90 = BigDecimal.ZERO;

        for (FinancialTransaction tx : problematicTxs) {
            long daysOverdue = ChronoUnit.DAYS.between(tx.getTransactionDate(), today);

            if (daysOverdue >= 1 && daysOverdue <= 30) {
                overdue30 = overdue30.add(tx.getAmount());
            } else if (daysOverdue >= 31 && daysOverdue <= 60) {
                overdue60 = overdue60.add(tx.getAmount());
            } else if (daysOverdue >= 61 && daysOverdue <= 90) {
                overdue90 = overdue90.add(tx.getAmount());
            } else if (daysOverdue > 90) {
                overdueOver90 = overdueOver90.add(tx.getAmount());
            }
        }
        BigDecimal currentReceivables = totalAccountsReceivable.subtract(overdue30).subtract(overdue60).subtract(overdue90).subtract(overdueOver90);
        ReceivablesAgingDto receivablesAging = new ReceivablesAgingDto(currentReceivables, overdue30, overdue60, overdue90, overdueOver90);

        FinancialOverviewDto overview = new FinancialOverviewDto(
                totalRevenueYTD,
                totalExpensesYTD,
                netIncomeYTD,
                totalAccountsReceivable,
                profitMargin
        );

        List<CashFlowTrendDto> cashFlowTrend = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            YearMonth month = YearMonth.from(today.minusMonths(i));
            LocalDate start = month.atDay(1);
            LocalDate end = month.atEndOfMonth();

            BigDecimal revenue = transactionRepository.sumCompletedTransactions(start, end).orElse(BigDecimal.ZERO);
            BigDecimal expenses = payrollRepository.sumPaidInDateRange(start, end)
                    .orElse(BigDecimal.ZERO)
                    .add(purchaseOrderRepository.sumApprovedOrdersInDateRange(start, end).orElse(BigDecimal.ZERO));

            cashFlowTrend.add(new CashFlowTrendDto(month.format(DateTimeFormatter.ofPattern("MMM/yy")), revenue, expenses, revenue.subtract(expenses)));
        }

        List<ExpenseBreakdownDto> expenseBreakdown = new ArrayList<>();
        if (totalExpensesYTD.compareTo(BigDecimal.ZERO) > 0) {
            double payrollPercentage = payrollExpensesYTD.divide(totalExpensesYTD, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).doubleValue();
            double purchasePercentage = purchaseExpensesYTD.divide(totalExpensesYTD, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).doubleValue();
            expenseBreakdown.add(new ExpenseBreakdownDto("Folha de Pagamento", payrollExpensesYTD, payrollPercentage));
            expenseBreakdown.add(new ExpenseBreakdownDto("Compras e Despesas Gerais", purchaseExpensesYTD, purchasePercentage));
        }

        return new DirectorFinancialReportDto(overview, cashFlowTrend, expenseBreakdown, receivablesAging);
    }
}
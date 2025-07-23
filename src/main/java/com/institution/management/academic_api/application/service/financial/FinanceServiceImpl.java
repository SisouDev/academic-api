package com.institution.management.academic_api.application.service.financial;

import com.institution.management.academic_api.application.dto.financial.PayableSummaryDto;
import com.institution.management.academic_api.domain.model.enums.common.PayrollStatus;
import com.institution.management.academic_api.domain.model.enums.financial.OrderStatus;
import com.institution.management.academic_api.domain.repository.common.PayrollRecordRepository;
import com.institution.management.academic_api.domain.repository.financial.PurchaseOrderRepository;
import com.institution.management.academic_api.domain.service.financial.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    private final PayrollRecordRepository payrollRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

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
}
package com.institution.management.academic_api.domain.service.financial;

import com.institution.management.academic_api.application.dto.dashboard.director.DirectorFinancialReportDto;
import com.institution.management.academic_api.application.dto.dashboard.director.TransactionDetailDto;
import com.institution.management.academic_api.application.dto.financial.PayableSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FinanceService {
    List<PayableSummaryDto> getPendingPayables();
    Page<TransactionDetailDto> findProblematicTransactions(Pageable pageable);
    DirectorFinancialReportDto getDirectorFinancialReport();
}
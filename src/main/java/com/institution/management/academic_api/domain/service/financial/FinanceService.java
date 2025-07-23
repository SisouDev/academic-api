package com.institution.management.academic_api.domain.service.financial;

import com.institution.management.academic_api.application.dto.financial.PayableSummaryDto;

import java.util.List;

public interface FinanceService {
    List<PayableSummaryDto> getPendingPayables();
}
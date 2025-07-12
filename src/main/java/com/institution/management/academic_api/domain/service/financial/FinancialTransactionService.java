package com.institution.management.academic_api.domain.service.financial;

import com.institution.management.academic_api.application.dto.financial.CreateFinancialTransactionRequestDto;
import com.institution.management.academic_api.application.dto.financial.FinancialTransactionDetailsDto;

import java.math.BigDecimal;
import java.util.List;

public interface FinancialTransactionService {

    FinancialTransactionDetailsDto create(CreateFinancialTransactionRequestDto dto);

    FinancialTransactionDetailsDto findById(Long id);

    List<FinancialTransactionDetailsDto> findTransactionsByStudent(Long studentId);

    BigDecimal getStudentBalance(Long studentId);
}
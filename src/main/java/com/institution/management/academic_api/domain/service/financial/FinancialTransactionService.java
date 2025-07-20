package com.institution.management.academic_api.domain.service.financial;

import com.institution.management.academic_api.application.dto.financial.CreateFinancialTransactionRequestDto;
import com.institution.management.academic_api.application.dto.financial.FinancialTransactionDetailsDto;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface FinancialTransactionService {

    FinancialTransactionDetailsDto create(CreateFinancialTransactionRequestDto dto);

    FinancialTransactionDetailsDto findById(Long id);

    List<FinancialTransactionDetailsDto> findTransactionsByPerson(Long studentId);

    BigDecimal getPersonBalance(Long studentId);

    Page<FinancialTransactionDetailsDto> findAll(TransactionType type, TransactionStatus status, Pageable pageable);
    FinancialTransactionDetailsDto markAsPaid(Long id);
}
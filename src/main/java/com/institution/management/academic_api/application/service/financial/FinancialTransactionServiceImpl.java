package com.institution.management.academic_api.application.service.financial;

import com.institution.management.academic_api.application.dto.financial.CreateFinancialTransactionRequestDto;
import com.institution.management.academic_api.application.dto.financial.FinancialTransactionDetailsDto;
import com.institution.management.academic_api.application.mapper.simple.financial.FinancialTransactionMapper;
import com.institution.management.academic_api.application.notifiers.financial.FinancialTransactionNotifier;
import com.institution.management.academic_api.domain.factory.financial.FinancialTransactionFactory;
import com.institution.management.academic_api.domain.model.entities.financial.FinancialTransaction;
import com.institution.management.academic_api.domain.model.entities.specification.FinancialTransactionSpecification;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionType;
import com.institution.management.academic_api.domain.repository.financial.FinancialTransactionRepository;
import com.institution.management.academic_api.domain.service.financial.FinancialTransactionService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancialTransactionServiceImpl implements FinancialTransactionService {

    private final FinancialTransactionRepository transactionRepository;
    private final FinancialTransactionFactory transactionFactory;
    private final FinancialTransactionMapper transactionMapper;
    private final FinancialTransactionNotifier transactionNotifier;

    @Override
    @Transactional
    @LogActivity("Registrou uma nova transação financeira.")
    public FinancialTransactionDetailsDto create(CreateFinancialTransactionRequestDto dto) {
        FinancialTransaction newTransaction = transactionFactory.create(dto);
        FinancialTransaction savedTransaction = transactionRepository.save(newTransaction);

        transactionNotifier.notifyStudentOfNewTransaction(savedTransaction);

        return transactionMapper.toDetailsDto(savedTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public FinancialTransactionDetailsDto findById(Long id) {
        return transactionRepository.findById(id)
                .map(transactionMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Financial transaction not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialTransactionDetailsDto> findTransactionsByPerson(Long studentId) {
        return transactionRepository.findByPersonIdOrderByTransactionDateDesc(studentId).stream()
                .map(transactionMapper::toDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getPersonBalance(Long studentId) {
        return transactionRepository.getBalanceForPerson(studentId).orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FinancialTransactionDetailsDto> findAll(TransactionType type, TransactionStatus status, Pageable pageable) {
        Specification<FinancialTransaction> spec = FinancialTransactionSpecification.filterBy(type, status);
        return transactionRepository.findAll(spec, pageable)
                .map(transactionMapper::toDetailsDto);
    }

    @Override
    @Transactional
    public FinancialTransactionDetailsDto markAsPaid(Long id) {
        FinancialTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada com o ID: " + id));

        transaction.setStatus(TransactionStatus.COMPLETED);
        FinancialTransaction updatedTransaction = transactionRepository.save(transaction);

        transactionNotifier.notifyUserOfPaidFine(updatedTransaction);

        return transactionMapper.toDetailsDto(updatedTransaction);
    }
}

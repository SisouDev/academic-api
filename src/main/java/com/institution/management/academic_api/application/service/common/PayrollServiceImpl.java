package com.institution.management.academic_api.application.service.common;

import com.institution.management.academic_api.application.dto.common.PayrollRecordDetailsDto;
import com.institution.management.academic_api.application.mapper.simple.common.PayrollRecordMapper;
import com.institution.management.academic_api.domain.factory.financial.FinancialTransactionFactory;
import com.institution.management.academic_api.domain.model.entities.common.PayrollRecord;
import com.institution.management.academic_api.domain.model.entities.financial.FinancialTransaction;
import com.institution.management.academic_api.domain.model.enums.common.PayrollStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionType;
import com.institution.management.academic_api.domain.repository.common.PayrollRecordRepository;
import com.institution.management.academic_api.domain.repository.financial.FinancialTransactionRepository;
import com.institution.management.academic_api.domain.service.common.PayrollService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRecordRepository payrollRepository;
    private final PayrollRecordMapper payrollMapper;
    private final FinancialTransactionRepository transactionRepository;
    private final FinancialTransactionFactory transactionFactory;

    @Override
    @Transactional(readOnly = true)
    public Page<PayrollRecordDetailsDto> findAll(PayrollStatus status, Pageable pageable) {
        Page<PayrollRecord> page;
        if (status != null) {
            page = payrollRepository.findAllByStatus(status, pageable);
        } else {
            page = payrollRepository.findAll(pageable);
        }
        return page.map(payrollMapper::toDetailsDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PayrollRecordDetailsDto findById(Long id) {
        return payrollRepository.findById(id)
                .map(payrollMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Registro de pagamento não encontrado com o ID: " + id));
    }

    @Override
    @Transactional
    public void markAsPaid(Long id) {
        PayrollRecord record = payrollRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de pagamento não encontrado com o ID: " + id));

        if (record.getStatus() == PayrollStatus.PAID) {
            throw new IllegalStateException("Este pagamento já foi processado.");
        }

        record.setStatus(PayrollStatus.PAID);
        record.setPaymentDate(LocalDate.now());
        payrollRepository.save(record);

        String personName = record.getPerson().getFirstName() + " " + record.getPerson().getLastName();
        String description = "Pagamento de Salário - " + personName + " - Ref: " + record.getReferenceMonth();

        FinancialTransaction transaction = transactionFactory.create(
                record.getPerson(),
                description,
                record.getNetPay().negate(),
                TransactionType.SALARY_PAYMENT,
                TransactionStatus.PAID
        );
        transactionRepository.save(transaction);
    }
}
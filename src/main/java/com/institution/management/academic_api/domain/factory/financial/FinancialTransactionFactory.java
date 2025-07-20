package com.institution.management.academic_api.domain.factory.financial;

import com.institution.management.academic_api.application.dto.financial.CreateFinancialTransactionRequestDto;
import com.institution.management.academic_api.application.mapper.simple.financial.FinancialTransactionMapper;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.financial.FinancialTransaction;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionType;
import com.institution.management.academic_api.domain.repository.student.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FinancialTransactionFactory {

    private final FinancialTransactionMapper transactionMapper;
    private final StudentRepository studentRepository;

    public FinancialTransaction create(CreateFinancialTransactionRequestDto dto) {
        FinancialTransaction transaction = transactionMapper.toEntity(dto);
        Student student = studentRepository.findById(dto.studentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + dto.studentId()));
        transaction.setPerson(student);

        transaction.setCreatedAt(LocalDateTime.now());

        return transaction;
    }

    public FinancialTransaction create(Person person, String description, BigDecimal amount, TransactionType type, TransactionStatus status) {
        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setPerson(person);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setStatus(status);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setCreatedAt(LocalDateTime.now());
        return transaction;
    }
}
package com.institution.management.academic_api.domain.factory.financial;

import com.institution.management.academic_api.application.dto.financial.CreateFinancialTransactionRequestDto;
import com.institution.management.academic_api.application.mapper.simple.financial.FinancialTransactionMapper;
import com.institution.management.academic_api.domain.model.entities.financial.FinancialTransaction;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.repository.student.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        transaction.setStudent(student);

        transaction.setCreatedAt(LocalDateTime.now());

        return transaction;
    }
}
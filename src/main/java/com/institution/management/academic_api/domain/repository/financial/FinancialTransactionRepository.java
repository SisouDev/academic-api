package com.institution.management.academic_api.domain.repository.financial;

import com.institution.management.academic_api.domain.model.entities.financial.FinancialTransaction;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {
    List<FinancialTransaction> findByStudentIdOrderByTransactionDateDesc(Long studentId);

    @Query("SELECT SUM(t.amount) FROM FinancialTransaction t WHERE t.student.id = :studentId")
    Optional<BigDecimal> getBalanceForStudent(Long studentId);

    long countByTypeAndStatus(TransactionType transactionType, TransactionStatus transactionStatus);
}
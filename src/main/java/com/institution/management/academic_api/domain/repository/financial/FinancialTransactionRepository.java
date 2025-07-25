package com.institution.management.academic_api.domain.repository.financial;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.financial.FinancialTransaction;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long>, JpaSpecificationExecutor<FinancialTransaction> {
    List<FinancialTransaction> findByPersonIdOrderByTransactionDateDesc(Long studentId);

    @Query("SELECT SUM(t.amount) FROM FinancialTransaction t WHERE t.person.id = :studentId")
    Optional<BigDecimal> getBalanceForPerson(Long studentId);

    long countByTypeAndStatus(TransactionType transactionType, TransactionStatus transactionStatus);

    boolean existsByPersonAndTypeAndTransactionDateBetween(Person person, TransactionType type, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(ft.amount) FROM FinancialTransaction ft WHERE ft.type = :type AND ft.status = 'PENDING' AND ft.transactionDate BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> calculateTotalAmountByTypeInDateRange(TransactionType type, LocalDate startDate, LocalDate endDate);

    long countByStatusIn(List<TransactionStatus> status);

    @Query("SELECT SUM(ft.amount) FROM FinancialTransaction ft WHERE ft.status = :status AND ft.transactionDate BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> calculateTotalAmountByStatusAndDateRange(
            @Param("status") TransactionStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    long countByType(TransactionType transactionType);

    long countByTypeAndStatusIn(TransactionType transactionType, List<TransactionStatus> pending);

    Page<FinancialTransaction> findByStatusIn(List<TransactionStatus> statuses, Pageable pageable);

    @Query("SELECT SUM(f.amount) FROM FinancialTransaction f WHERE f.status = com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus.COMPLETED AND f.transactionDate BETWEEN :start AND :end")
    Optional<BigDecimal> sumCompletedTransactions(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT SUM(f.amount) FROM FinancialTransaction f WHERE f.status IN (com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus.PENDING, com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus.FAILED)")
    Optional<BigDecimal> sumProblematicTransactions();

    @Query("SELECT f FROM FinancialTransaction f WHERE f.status IN (com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus.PENDING, com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus.FAILED)")
    List<FinancialTransaction> findAllProblematicTransactions();
}

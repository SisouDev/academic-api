package com.institution.management.academic_api.domain.repository.library;

import com.institution.management.academic_api.domain.model.entities.library.Loan;
import com.institution.management.academic_api.domain.model.enums.library.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {
    List<Loan> findByBorrowerId(Long borrowerId);

    List<Loan> findByItemId(Long itemId);

    List<Loan> findByStatus(LoanStatus status);

    List<Loan> findByStatusAndDueDateBefore(LoanStatus status, LocalDate today);

    long countByStatus(LoanStatus status);

    long countByStatusAndReturnDateBefore(LoanStatus status, LocalDate currentDate);

}

package com.institution.management.academic_api.domain.repository.common;

import com.institution.management.academic_api.domain.model.entities.common.PayrollRecord;
import com.institution.management.academic_api.domain.model.enums.common.PayrollStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRecordRepository extends JpaRepository<PayrollRecord, Long> {

    boolean existsByPersonIdAndReferenceMonth(Long personId, LocalDate referenceMonth);

    Page<PayrollRecord> findAllByStatus(PayrollStatus status, Pageable pageable);

    long countByStatus(PayrollStatus status);

    @Query("SELECT SUM(pr.netPay) FROM PayrollRecord pr WHERE pr.status = :status AND pr.referenceMonth BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> calculateTotalNetPayByStatusAndDateRange(PayrollStatus status, LocalDate startDate, LocalDate endDate);

    List<PayrollRecord> findAllByStatus(PayrollStatus status);

    @Query("SELECT SUM(p.netPay) FROM PayrollRecord p WHERE p.status = com.institution.management.academic_api.domain.model.enums.common.PayrollStatus.PAID AND p.paymentDate BETWEEN :start AND :end")
    Optional<BigDecimal> sumPaidInDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);
}

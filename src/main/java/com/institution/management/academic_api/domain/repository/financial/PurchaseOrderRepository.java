package com.institution.management.academic_api.domain.repository.financial;

import com.institution.management.academic_api.domain.model.entities.financial.PurchaseOrder;
import com.institution.management.academic_api.domain.model.enums.financial.OrderStatus;
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
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    Page<PurchaseOrder> findAllByStatus(OrderStatus status, Pageable pageable);

    long countByStatus(OrderStatus status);

    List<PurchaseOrder> findAllByStatus(OrderStatus status);

    @Query("SELECT SUM(po.amount) FROM PurchaseOrder po WHERE po.status IN (com.institution.management.academic_api.domain.model.enums.financial.OrderStatus.APPROVED, com.institution.management.academic_api.domain.model.enums.financial.OrderStatus.PAID) AND po.orderDate BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> sumApprovedOrdersInDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
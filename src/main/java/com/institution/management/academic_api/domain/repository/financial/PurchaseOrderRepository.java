package com.institution.management.academic_api.domain.repository.financial;

import com.institution.management.academic_api.domain.model.entities.financial.PurchaseOrder;
import com.institution.management.academic_api.domain.model.enums.financial.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    Page<PurchaseOrder> findAllByStatus(OrderStatus status, Pageable pageable);

    long countByStatus(OrderStatus status);

    List<PurchaseOrder> findAllByStatus(OrderStatus status);


}
package com.institution.management.academic_api.domain.service.financial;

import com.institution.management.academic_api.application.dto.financial.CreatePurchaseOrderRequestDto;
import com.institution.management.academic_api.application.dto.financial.PurchaseOrderDto;
import com.institution.management.academic_api.domain.model.enums.financial.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseOrderService {
    PurchaseOrderDto create(CreatePurchaseOrderRequestDto dto, String requesterEmail);
    Page<PurchaseOrderDto> findAll(OrderStatus status, Pageable pageable);
    PurchaseOrderDto findById(Long id);
    void updateStatus(Long id, OrderStatus status);
}
package com.institution.management.academic_api.domain.factory.financial;

import com.institution.management.academic_api.application.dto.financial.CreatePurchaseOrderRequestDto;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.financial.PurchaseOrder;
import com.institution.management.academic_api.domain.model.enums.financial.OrderStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PurchaseOrderFactory {
    public PurchaseOrder create(CreatePurchaseOrderRequestDto dto, Employee requester) {
        PurchaseOrder po = new PurchaseOrder();
        po.setRequester(requester);
        po.setSupplier(dto.supplier());
        po.setDescription(dto.description());
        po.setAmount(dto.amount());
        po.setDueDate(dto.dueDate());
        po.setOrderDate(LocalDate.now());
        po.setCreatedAt(LocalDateTime.now());
        po.setStatus(OrderStatus.PENDING_APPROVAL);
        return po;
    }
}
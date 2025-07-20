package com.institution.management.academic_api.application.service.financial;

import com.institution.management.academic_api.application.dto.financial.CreatePurchaseOrderRequestDto;
import com.institution.management.academic_api.application.dto.financial.PurchaseOrderDto;
import com.institution.management.academic_api.application.mapper.simple.financial.PurchaseOrderMapper;
import com.institution.management.academic_api.domain.factory.financial.FinancialTransactionFactory;
import com.institution.management.academic_api.domain.factory.financial.PurchaseOrderFactory;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.financial.FinancialTransaction;
import com.institution.management.academic_api.domain.model.entities.financial.PurchaseOrder;
import com.institution.management.academic_api.domain.model.enums.financial.OrderStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionStatus;
import com.institution.management.academic_api.domain.model.enums.financial.TransactionType;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.financial.FinancialTransactionRepository;
import com.institution.management.academic_api.domain.repository.financial.PurchaseOrderRepository;
import com.institution.management.academic_api.domain.service.financial.PurchaseOrderService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository poRepository;
    private final EmployeeRepository employeeRepository;
    private final FinancialTransactionRepository transactionRepository;
    private final PurchaseOrderFactory poFactory;
    private final FinancialTransactionFactory transactionFactory;
    private final PurchaseOrderMapper poMapper;

    @Override
    @Transactional
    public PurchaseOrderDto create(CreatePurchaseOrderRequestDto dto, String requesterEmail) {
        Employee requester = employeeRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário solicitante não encontrado: " + requesterEmail));

        PurchaseOrder newPO = poFactory.create(dto, requester);
        PurchaseOrder savedPO = poRepository.save(newPO);
        return poMapper.toDto(savedPO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseOrderDto> findAll(OrderStatus status, Pageable pageable) {
        Page<PurchaseOrder> page = (status != null)
                ? poRepository.findAllByStatus(status, pageable)
                : poRepository.findAll(pageable);
        return page.map(poMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderDto findById(Long id) {
        return poRepository.findById(id)
                .map(poMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Compra não encontrada com o ID: " + id));
    }

    @Override
    @Transactional
    public void updateStatus(Long id, OrderStatus status) {
        PurchaseOrder po = poRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Compra não encontrada com o ID: " + id));

        po.setStatus(status);

        if (status == OrderStatus.PAID) {
            po.setPaymentDate(LocalDate.now());

            String description = "Pagamento - Ordem de Compra #" + po.getId() + ": " + po.getDescription();
            FinancialTransaction transaction = transactionFactory.create(
                    po.getRequester(),
                    description,
                    po.getAmount().negate(),
                    TransactionType.PURCHASE,
                    TransactionStatus.PAID
            );
            transactionRepository.save(transaction);
        }

        poRepository.save(po);
    }
}
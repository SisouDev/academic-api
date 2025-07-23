package com.institution.management.academic_api.application.service.financial;

import com.institution.management.academic_api.application.dto.financial.CreatePurchaseOrderRequestDto;
import com.institution.management.academic_api.application.dto.financial.CreatePurchaseRequestDto;
import com.institution.management.academic_api.application.dto.financial.PurchaseRequestDto;
import com.institution.management.academic_api.application.mapper.simple.financial.PurchaseRequestMapper;
import com.institution.management.academic_api.application.notifiers.financial.PurchaseRequestNotifier;
import com.institution.management.academic_api.domain.factory.financial.PurchaseOrderFactory;
import com.institution.management.academic_api.domain.factory.financial.PurchaseRequestFactory;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.financial.PurchaseOrder;
import com.institution.management.academic_api.domain.model.entities.financial.PurchaseRequest;
import com.institution.management.academic_api.domain.model.enums.financial.PurchaseRequestStatus;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.financial.PurchaseOrderRepository;
import com.institution.management.academic_api.domain.repository.financial.PurchaseRequestRepository;
import com.institution.management.academic_api.domain.service.financial.PurchaseRequestService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PurchaseRequestServiceImpl implements PurchaseRequestService {

    private final PurchaseRequestRepository requestRepository;
    private final EmployeeRepository employeeRepository;
    private final PurchaseRequestFactory requestFactory;
    private final PurchaseRequestMapper requestMapper;
    private final PurchaseOrderRepository poRepository;
    private final PurchaseOrderFactory poFactory;
    private final PurchaseRequestNotifier notifier;

    @Override
    @Transactional
    public PurchaseRequestDto create(CreatePurchaseRequestDto dto, String requesterEmail) {
        Employee requester = employeeRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário solicitante não encontrado: " + requesterEmail));

        PurchaseRequest newRequest = requestFactory.create(dto, requester);
        PurchaseRequest savedRequest = requestRepository.save(newRequest);
        notifier.notifyFinanceAssistantsOfNewRequest(savedRequest);

        return requestMapper.toDto(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseRequestDto findById(Long id) {
        return requestRepository.findById(id)
                .map(requestMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Requisição de Compra não encontrada com o ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseRequestDto> findAll(PurchaseRequestStatus status, Pageable pageable) {
        Page<PurchaseRequest> page = (status != null)
                ? requestRepository.findAllByStatus(status, pageable)
                : requestRepository.findAll(pageable);
        return page.map(requestMapper::toDto);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, PurchaseRequestStatus newStatus) {
        PurchaseRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Requisição de Compra não encontrada com o ID: " + id));

        request.setStatus(newStatus);

        if (newStatus == PurchaseRequestStatus.APPROVED_BY_ASSISTANT) {

            CreatePurchaseOrderRequestDto poDto = new CreatePurchaseOrderRequestDto(
                    "A definir pelo Financeiro",
                    String.format("Item: %s (Qtde: %d). Justificativa: %s",
                            request.getItemName(), request.getQuantity(), request.getJustification()),
                    new java.math.BigDecimal("0.00"),
                    java.time.LocalDate.now().plusDays(30)
            );

            PurchaseOrder newPO = poFactory.create(poDto, request.getRequester());

            newPO.setPurchaseRequest(request);

            poRepository.save(newPO);

            request.setStatus(PurchaseRequestStatus.PROCESSED);
        }
        notifier.notifyRequesterOfReview(request);
        requestRepository.save(request);
    }
}
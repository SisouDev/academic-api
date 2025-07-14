package com.institution.management.academic_api.application.service.request;

import com.institution.management.academic_api.application.dto.request.CreateInternalRequestDto;
import com.institution.management.academic_api.application.dto.request.InternalRequestDetailsDto;
import com.institution.management.academic_api.application.dto.request.UpdateInternalRequestDto;
import com.institution.management.academic_api.application.mapper.simple.request.InternalRequestMapper;
import com.institution.management.academic_api.application.notifiers.request.InternalRequestNotifier;
import com.institution.management.academic_api.application.service.utils.RoundRobinAssignerService;
import com.institution.management.academic_api.domain.factory.request.InternalRequestFactory;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.request.InternalRequest;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import com.institution.management.academic_api.domain.model.enums.request.RequestStatus;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.request.InternalRequestRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import com.institution.management.academic_api.domain.service.request.InternalRequestService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import com.institution.management.academic_api.infra.utils.HtmlSanitizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InternalRequestServiceImpl implements InternalRequestService {

    private final InternalRequestRepository requestRepository;
    private final PersonRepository personRepository;
    private final InternalRequestFactory requestFactory;
    private final InternalRequestMapper requestMapper;
    private final NotificationService notificationService;
    private final InternalRequestNotifier requestNotifier;
    private final RoundRobinAssignerService roundRobinAssigner;
    private final HtmlSanitizerService htmlSanitizerService;


    @Override
    @Transactional
    @LogActivity("Abriu uma nova requisição interna.")
    public InternalRequestDetailsDto create(CreateInternalRequestDto dto, String requesterEmail) {
        Person requester = personRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new EntityNotFoundException("Applicant not found with email: " + requesterEmail));

        InternalRequest newRequest = requestFactory.create(dto, requester);
        Employee handler = roundRobinAssigner.getNextHandlerByJobPosition(JobPosition.TECHNICIAN);

        if (handler != null) {
            newRequest.setHandler(handler);
            newRequest.setStatus(RequestStatus.IN_PROGRESS);
        }

        String safeDescription = htmlSanitizerService.sanitize(dto.description());
        newRequest.setDescription(safeDescription);

        InternalRequest savedRequest = requestRepository.save(newRequest);

        requestNotifier.notifyDepartmentOfNewRequest(savedRequest);
        if (handler != null) {
            requestNotifier.notifyRequesterOfAssignment(savedRequest);
        }

        return requestMapper.toDetailsDto(savedRequest);
    }

    @Override
    @Transactional
    @LogActivity("Atribuiu uma requisição interna.")
    public void assignRequest(Long requestId, Long handlerId) {
        InternalRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found with id: " + requestId));

        Person handler = personRepository.findById(handlerId)
                .orElseThrow(() -> new EntityNotFoundException("Responsible not found with id: " + handlerId));

        request.setHandler(handler);
        request.setStatus(RequestStatus.IN_PROGRESS);

        requestNotifier.notifyRequesterOfAssignment(request);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou o status de uma requisição interna.")
    public InternalRequestDetailsDto updateStatus(Long requestId, UpdateInternalRequestDto dto) {
        InternalRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found with id: " + requestId));

        requestMapper.updateFromDto(dto, request);

        RequestStatus newStatus = RequestStatus.valueOf(dto.status().toUpperCase());
        if (newStatus == RequestStatus.COMPLETED || newStatus == RequestStatus.REJECTED) {
            request.setResolvedAt(LocalDateTime.now());
        }

        requestNotifier.notifyRequesterOfStatusChange(request);

        return requestMapper.toDetailsDto(request);
    }

    @Override
    @Transactional(readOnly = true)
    public InternalRequestDetailsDto findById(Long id) {
        return requestRepository.findById(id)
                .map(requestMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Request not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InternalRequestDetailsDto> findMyRequests(String userEmail) {
        Person requester = personRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Applicant not found with email: " + userEmail));

        return requestRepository.findByRequesterId(requester.getId()).stream()
                .map(requestMapper::toDetailsDto)
                .collect(Collectors.toList());
    }
}

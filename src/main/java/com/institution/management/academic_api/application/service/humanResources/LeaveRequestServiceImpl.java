package com.institution.management.academic_api.application.service.humanResources;

import com.institution.management.academic_api.application.dto.humanResources.CreateLeaveRequestDto;
import com.institution.management.academic_api.application.dto.humanResources.LeaveRequestDetailsDto;
import com.institution.management.academic_api.application.dto.humanResources.ReviewLeaveRequestDto;
import com.institution.management.academic_api.application.mapper.simple.humanResources.LeaveRequestMapper;
import com.institution.management.academic_api.application.notifiers.humanResources.LeaveRequestNotifier;
import com.institution.management.academic_api.domain.factory.humanResources.LeaveRequestFactory;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.humanResources.LeaveRequest;
import com.institution.management.academic_api.domain.model.enums.humanResources.LeaveRequestStatus;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.humanResources.LeaveRequestRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import com.institution.management.academic_api.domain.service.humanResources.LeaveRequestService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestFactory leaveRequestFactory;
    private final LeaveRequestMapper leaveRequestMapper;
    private final NotificationService notificationService;
    private final LeaveRequestNotifier leaveRequestNotifier;

    @Override
    @Transactional
    @LogActivity("Criou uma nova solicitação de ausência.")
    public LeaveRequestDetailsDto create(CreateLeaveRequestDto dto, String requesterEmail) {
        Employee requester = employeeRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new EntityNotFoundException("Requesting employee not found with email: " + requesterEmail));

        LeaveRequest newLeaveRequest = leaveRequestFactory.create(dto, requester);
        LeaveRequest savedLeaveRequest = leaveRequestRepository.save(newLeaveRequest);

        leaveRequestNotifier.notifyHrOfNewLeaveRequest(savedLeaveRequest);

        return leaveRequestMapper.toDetailsDto(savedLeaveRequest);
    }

    @Override
    @Transactional
    @LogActivity("Analisou uma solicitação de ausência.")
    public void review(Long leaveRequestId, ReviewLeaveRequestDto dto, String reviewerEmail) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Absence request not found with id: " + leaveRequestId));

        Employee reviewer = employeeRepository.findByEmail(reviewerEmail)
                .orElseThrow(() -> new EntityNotFoundException("Reviewer not found with email: " + reviewerEmail));

        leaveRequest.setStatus(LeaveRequestStatus.valueOf(dto.status().toUpperCase()));
        leaveRequest.setReviewer(reviewer);
        leaveRequest.setReviewedAt(LocalDateTime.now());

        leaveRequestNotifier.notifyRequesterOfReview(leaveRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveRequestDetailsDto findById(Long id) {
        return leaveRequestRepository.findById(id)
                .map(leaveRequestMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Absence request not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestDetailsDto> findByRequester(Long requesterId) {
        return leaveRequestRepository.findByRequesterId(requesterId).stream()
                .map(leaveRequestMapper::toDetailsDto)
                .collect(Collectors.toList());
    }
}

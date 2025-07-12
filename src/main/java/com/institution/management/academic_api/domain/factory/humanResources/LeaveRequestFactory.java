package com.institution.management.academic_api.domain.factory.humanResources;

import com.institution.management.academic_api.application.dto.humanResources.CreateLeaveRequestDto;
import com.institution.management.academic_api.application.mapper.simple.humanResources.LeaveRequestMapper;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.humanResources.LeaveRequest;
import com.institution.management.academic_api.domain.model.enums.humanResources.LeaveRequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LeaveRequestFactory {

    private final LeaveRequestMapper leaveRequestMapper;

    public LeaveRequest create(CreateLeaveRequestDto dto, Employee requester) {
        LeaveRequest leaveRequest = leaveRequestMapper.toEntity(dto);

        leaveRequest.setCreatedAt(LocalDateTime.now());
        leaveRequest.setStatus(LeaveRequestStatus.PENDING);
        leaveRequest.setReviewer(null);
        leaveRequest.setReviewedAt(null);

        leaveRequest.setRequester(requester);

        return leaveRequest;
    }
}
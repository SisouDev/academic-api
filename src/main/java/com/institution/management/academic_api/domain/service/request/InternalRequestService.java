package com.institution.management.academic_api.domain.service.request;

import com.institution.management.academic_api.application.dto.request.CreateInternalRequestDto;
import com.institution.management.academic_api.application.dto.request.InternalRequestDetailsDto;
import com.institution.management.academic_api.application.dto.request.UpdateInternalRequestDto;

import java.util.List;

public interface InternalRequestService {
    InternalRequestDetailsDto create(CreateInternalRequestDto dto, String requesterEmail);

    void assignRequest(Long requestId, Long handlerId);

    InternalRequestDetailsDto updateStatus(Long requestId, UpdateInternalRequestDto dto);

    InternalRequestDetailsDto findById(Long id);

    List<InternalRequestDetailsDto> findMyRequests(String userEmail);
}

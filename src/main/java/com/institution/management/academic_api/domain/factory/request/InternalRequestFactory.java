package com.institution.management.academic_api.domain.factory.request;

import com.institution.management.academic_api.application.dto.request.CreateInternalRequestDto;
import com.institution.management.academic_api.application.mapper.simple.request.InternalRequestMapper;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.request.InternalRequest;
import com.institution.management.academic_api.domain.model.enums.request.RequestStatus;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InternalRequestFactory {

    private final InternalRequestMapper requestMapper;
    private final DepartmentRepository departmentRepository;

    public InternalRequest create(CreateInternalRequestDto dto, Person requester) {
        InternalRequest request = requestMapper.toEntity(dto);

        request.setCreatedAt(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);
        request.setHandler(null);
        request.setResolvedAt(null);
        request.setResolutionNotes(null);

        request.setRequester(requester);

        if (dto.targetDepartmentId() != null) {
            Department targetDepartment = departmentRepository.findById(dto.targetDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + dto.targetDepartmentId()));
            request.setTargetDepartment(targetDepartment);
        }

        return request;
    }
}
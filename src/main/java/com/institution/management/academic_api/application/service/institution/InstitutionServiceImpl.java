package com.institution.management.academic_api.application.service.institution;

import com.institution.management.academic_api.application.dto.institution.CreateInstitutionRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionDetailsDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionSummaryDto;
import com.institution.management.academic_api.application.dto.institution.UpdateInstitutionRequestDto;
import com.institution.management.academic_api.application.mapper.simple.institution.InstitutionMapper;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.domain.service.institution.InstitutionService;
import com.institution.management.academic_api.exception.type.institution.InstitutionAlreadyExistsException;
import com.institution.management.academic_api.exception.type.institution.InstitutionCannotBeDeletedException;
import com.institution.management.academic_api.exception.type.institution.InstitutionNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstitutionServiceImpl implements InstitutionService {
    private final InstitutionMapper institutionMapper;
    private final InstitutionRepository institutionRepository;

    @Override
    @Transactional
    @LogActivity("Criou uma nova instituição.")
    public InstitutionDetailsDto create(CreateInstitutionRequestDto request) {
        institutionRepository.findByRegisterId(request.registerId()).ifPresent(inst -> {
            throw new InstitutionAlreadyExistsException("An institution with the registration ID " + request.registerId() + " already exists.");
        });
        Institution newInstitution = institutionMapper.toEntity(request);
        newInstitution.setCreatedAt(LocalDateTime.now());
        Institution savedInstitution = institutionRepository.save(newInstitution);

        return institutionMapper.toDetailsDto(savedInstitution);
    }

    @Override
    @Transactional(readOnly = true)
    public InstitutionDetailsDto findById(Long id) {
        return institutionMapper.toDetailsDto(findInstitutionByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstitutionSummaryDto> findAll() {
        return institutionRepository.findAll().stream()
                .map(institutionMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @LogActivity("Atualizou uma instituição.")
    public InstitutionDetailsDto update(Long id, UpdateInstitutionRequestDto request) {
        Institution institutionToUpdate = findInstitutionByIdOrThrow(id);

        institutionMapper.updateFromDto(request, institutionToUpdate);

        Institution updatedInstitution = institutionRepository.save(institutionToUpdate);
        return institutionMapper.toDetailsDto(updatedInstitution);
    }

    @Override
    @Transactional
    @LogActivity("Deletou uma instituição.")
    public void delete(Long id) {
        Institution institutionToDelete = findInstitutionByIdOrThrow(id);
        if (!institutionToDelete.getDepartments().isEmpty() || !institutionToDelete.getMembers().isEmpty()) {
            throw new InstitutionCannotBeDeletedException("It is not possible to delete the institution because it has associated departments or members.");
        }
        institutionRepository.delete(institutionToDelete);
    }

    private Institution findInstitutionByIdOrThrow(Long id) {
        return institutionRepository.findById(id)
                .orElseThrow(() -> new InstitutionNotFoundException("Institution not found with id: " + id));
    }
}

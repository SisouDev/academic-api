package com.institution.management.academic_api.application.service.financial;

import com.institution.management.academic_api.application.dto.financial.CreateScholarshipRequestDto;
import com.institution.management.academic_api.application.dto.financial.ScholarshipDetailsDto;
import com.institution.management.academic_api.application.mapper.simple.financial.ScholarshipMapper;
import com.institution.management.academic_api.domain.factory.financial.ScholarshipFactory;
import com.institution.management.academic_api.domain.model.entities.financial.Scholarship;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.enums.financial.ScholarshipStatus;
import com.institution.management.academic_api.domain.repository.financial.ScholarshipRepository;
import com.institution.management.academic_api.domain.repository.student.EnrollmentRepository;
import com.institution.management.academic_api.domain.service.financial.ScholarshipService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScholarshipServiceImpl implements ScholarshipService {

    private final ScholarshipRepository scholarshipRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ScholarshipFactory scholarshipFactory;
    private final ScholarshipMapper scholarshipMapper;

    @Override
    @Transactional
    public ScholarshipDetailsDto create(CreateScholarshipRequestDto dto) {
        Enrollment enrollment = enrollmentRepository.findById(dto.enrollmentId())
                .orElseThrow(() -> new EntityNotFoundException("Matrícula não encontrada com o ID: " + dto.enrollmentId()));

        Scholarship newScholarship = scholarshipFactory.create(dto, enrollment);
        Scholarship savedScholarship = scholarshipRepository.save(newScholarship);
        return scholarshipMapper.toDetailsDto(savedScholarship);
    }

    @Override
    @Transactional(readOnly = true)
    public ScholarshipDetailsDto findById(Long id) {
        return scholarshipRepository.findById(id)
                .map(scholarshipMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Bolsa não encontrada com o ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScholarshipDetailsDto> findByEnrollment(Long enrollmentId) {
        return scholarshipRepository.findByEnrollmentId(enrollmentId).stream()
                .map(scholarshipMapper::toDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateStatus(Long id, ScholarshipStatus status) {
        Scholarship scholarship = scholarshipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bolsa não encontrada com o ID: " + id));
        scholarship.setStatus(status);
        scholarshipRepository.save(scholarship);
    }
}
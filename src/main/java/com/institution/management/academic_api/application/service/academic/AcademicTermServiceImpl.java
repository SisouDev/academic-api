package com.institution.management.academic_api.application.service.academic;

import com.institution.management.academic_api.application.dto.academic.AcademicTermDetailsDto;
import com.institution.management.academic_api.application.dto.academic.AcademicTermRequestDto;
import com.institution.management.academic_api.application.dto.academic.AcademicTermSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateAcademicTermRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.AcademicTermMapper;
import com.institution.management.academic_api.application.notifiers.academic.AcademicTermNotifier;
import com.institution.management.academic_api.domain.model.entities.academic.AcademicTerm;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.repository.academic.AcademicTermRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.domain.service.academic.AcademicTermService;
import com.institution.management.academic_api.exception.type.academic.AcademicTermAlreadyExistsException;
import com.institution.management.academic_api.exception.type.academic.AcademicTermStatusChangeNotAllowedException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AcademicTermServiceImpl implements AcademicTermService {

    private final AcademicTermRepository academicTermRepository;
    private final InstitutionRepository institutionRepository;
    private final AcademicTermMapper academicTermMapper;
    private final AcademicTermNotifier academicTermNotifier;

    @Override
    @Transactional
    @LogActivity("Cadastrou um novo período acadêmico.")
    public AcademicTermDetailsDto create(AcademicTermRequestDto request) {
        Institution institution = institutionRepository.findById(request.institutionId())
                .orElseThrow(() -> new EntityNotFoundException("Institution not found with id: " + request.institutionId()));

        if (academicTermRepository.existsByYearAndSemesterAndInstitution(request.year(), request.semester(), institution)) {
            throw new AcademicTermAlreadyExistsException("Academic Term for this year/semester already exists.");
        }

        AcademicTerm newAcademicTerm = academicTermMapper.toEntity(request);
        newAcademicTerm.setInstitution(institution);

        AcademicTerm savedAcademicTerm = academicTermRepository.save(newAcademicTerm);
        academicTermNotifier.notifyAdminOfNewTerm(savedAcademicTerm);
        return academicTermMapper.toDetailsDto(savedAcademicTerm);
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicTermDetailsDto findById(Long id) {
        return academicTermRepository.findById(id)
                .map(academicTermMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Academic Term not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcademicTermSummaryDto> findPaginated(Long institutionId, Pageable pageable) {
        if (institutionId == null) {
            return academicTermRepository.findAll(pageable).map(academicTermMapper::toSummaryDto);
        }
        return academicTermRepository.findAllByInstitutionId(institutionId, pageable)
                .map(academicTermMapper::toSummaryDto);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou um período acadêmico.")
    public AcademicTermDetailsDto update(Long id, UpdateAcademicTermRequestDto request) {
        AcademicTerm academicTermToUpdate = academicTermRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Academic Term not found with id: " + id));

        academicTermMapper.updateFromDto(request, academicTermToUpdate);
        AcademicTerm updatedAcademicTerm = academicTermRepository.save(academicTermToUpdate);
        academicTermNotifier.notifyAdminOfTermUpdate(academicTermToUpdate);
        return academicTermMapper.toDetailsDto(updatedAcademicTerm);
    }

    @Override
    @Transactional
    @LogActivity("Deletou um período acadêmico.")
    public void delete(Long id) {
        AcademicTerm academicTermToDelete = academicTermRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Academic Term not found with id: " + id));

        if (!academicTermToDelete.getCourseSections().isEmpty()) {
            throw new AcademicTermStatusChangeNotAllowedException("Cannot delete this Academic Term because it has associated Course Sections.");
        }
        academicTermNotifier.notifyAdminOfTermDeletion(academicTermToDelete);
        academicTermRepository.delete(academicTermToDelete);
    }
}
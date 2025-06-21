package com.institution.management.academic_api.application.service.academic;

import com.institution.management.academic_api.application.dto.academic.DepartmentDetailsDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentRequestDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateDepartmentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.DepartmentMapper;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.domain.service.academic.DepartmentService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.exception.type.institution.InstitutionNotFoundException;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentMapper departmentMapper;
    private final DepartmentRepository departmentRepository;
    private final InstitutionRepository institutionRepository;


    @Override
    @Transactional
    public DepartmentDetailsDto create(DepartmentRequestDto request) {
        Institution institution = findInstitutionByIdOrThrow(request.institutionId());
        if(departmentRepository.existsByNameAndInstitution(request.name(), institution)){
            throw new EntityExistsException("Department already exists.");
        }
        Department newDepartment = departmentMapper.toEntity(request);
        newDepartment.setInstitution(institution);

        Department savedDepartment = departmentRepository.save(newDepartment);

        return departmentMapper.toDetailsDto(savedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDetailsDto findById(Long id) {
        Department department = findDepartmentByIdOrThrow(id);
        return departmentMapper.toDetailsDto(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentSummaryDto> findAllByInstitution(Long institutionId) {
        Institution institution = findInstitutionByIdOrThrow(institutionId);
        List<Department> departments = departmentRepository.findAllByInstitution(institution);
        return departments.stream()
                .map(departmentMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DepartmentDetailsDto update(Long id, UpdateDepartmentRequestDto request) {
        Department departmentToUpdate = findDepartmentByIdOrThrow(id);
        departmentMapper.updateFromDto(request, departmentToUpdate);
        Department updatedDepartment = departmentRepository.save(departmentToUpdate);
        return departmentMapper.toDetailsDto(updatedDepartment);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Department departmentToDelete = findDepartmentByIdOrThrow(id);
        departmentRepository.delete(departmentToDelete);
    }

    private Department findDepartmentByIdOrThrow(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
    }

    private Institution findInstitutionByIdOrThrow(Long id) {
        return institutionRepository.findById(id)
                .orElseThrow(() -> new InstitutionNotFoundException("Institution not found with id: " + id));
    }
}

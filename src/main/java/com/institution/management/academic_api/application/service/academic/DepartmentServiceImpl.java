package com.institution.management.academic_api.application.service.academic;

import com.institution.management.academic_api.application.dto.academic.DepartmentDetailsDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentRequestDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateDepartmentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.DepartmentMapper;
import com.institution.management.academic_api.application.notifiers.academic.DepartmentNotifier;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.academic.DepartmentService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.exception.type.institution.InstitutionNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;
    private final DepartmentNotifier departmentNotifier;


    @Override
    @Transactional
    @LogActivity("Cadastrou um novo departamento acadêmico.")
    public DepartmentDetailsDto create(DepartmentRequestDto request) {
        Institution institution = findInstitutionByIdOrThrow(request.institutionId());
        if(departmentRepository.existsByNameAndInstitution(request.name(), institution)){
            throw new EntityExistsException("Department already exists.");
        }
        Department newDepartment = departmentMapper.toEntity(request);
        newDepartment.setInstitution(institution);

        Department savedDepartment = departmentRepository.save(newDepartment);

        departmentNotifier.notifyAdminOfNewDepartment(savedDepartment);
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
    public List<DepartmentSummaryDto> findAllForSelection() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toSummaryDto)
                .collect(Collectors.toList());
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
    @LogActivity("Atualizou um departamento acadêmico.")
    public DepartmentDetailsDto update(Long id, UpdateDepartmentRequestDto request) {
        Department departmentToUpdate = findDepartmentByIdOrThrow(id);
        String oldName = departmentToUpdate.getName();
        departmentMapper.updateFromDto(request, departmentToUpdate);
        Department updatedDepartment = departmentRepository.save(departmentToUpdate);
        departmentNotifier.notifyAdminOfDepartmentUpdate(oldName, departmentToUpdate);
        return departmentMapper.toDetailsDto(updatedDepartment);
    }

    @Override
    @Transactional
    @LogActivity("Deletou um departamento acadêmico.")
    public void delete(Long id) {
        Department departmentToDelete = findDepartmentByIdOrThrow(id);
        departmentNotifier.notifyAdminOfDepartmentDeletion(departmentToDelete);
        departmentRepository.delete(departmentToDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentSummaryDto> findAllForCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByLogin(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário logado não encontrado."));

        Institution institution = currentUser.getPerson().getInstitution();
        List<Department> departments = departmentRepository.findAllByInstitution(institution);

        return departments.stream()
                .map(departmentMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentSummaryDto> findAll(String searchTerm, Pageable pageable) {
        Specification<Department> spec = (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + searchTerm.toLowerCase() + "%");
        };
        return departmentRepository.findAll(spec, pageable)
                .map(departmentMapper::toSummaryDto);
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

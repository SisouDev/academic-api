package com.institution.management.academic_api.application.service.employee;

import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
import com.institution.management.academic_api.application.dto.employee.CreateEmployeeRequestDto;
import com.institution.management.academic_api.application.dto.employee.EmployeeResponseDto;
import com.institution.management.academic_api.application.dto.employee.EmployeeSummaryDto;
import com.institution.management.academic_api.application.dto.employee.UpdateEmployeeRequestDto;
import com.institution.management.academic_api.application.dto.institution.CreateInstitutionAdminRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionAdminResponseDto;
import com.institution.management.academic_api.application.dto.user.CreateUserRequestDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.application.mapper.simple.employee.EmployeeMapper;
import com.institution.management.academic_api.application.mapper.simple.institution.InstitutionAdminMapper;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.common.Role;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.entities.institution.InstitutionAdmin;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.common.RoleRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionAdminRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.employee.EmployeeService;
import com.institution.management.academic_api.domain.service.user.UserService;
import com.institution.management.academic_api.exception.type.common.EmailAlreadyExists;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.exception.type.employee.EmployeeNotFoundException;
import com.institution.management.academic_api.exception.type.institution.InstitutionNotFoundException;
import com.institution.management.academic_api.exception.type.user.InvalidRoleAssignmentException;
import com.institution.management.academic_api.exception.type.user.UserNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final InstitutionAdminRepository institutionAdminRepository;
    private final InstitutionAdminMapper institutionAdminMapper;

    private final InstitutionRepository institutionRepository;
    private final UserService userService;
    private final PersonMapper personMapper;
    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    @LogActivity("Criou um novo funcionário.")
    public EmployeeResponseDto createEmployee(CreateEmployeeRequestDto request) {
        Institution institution = findInstitutionByIdOrThrow(request.institutionId());
        if (employeeRepository.existsEmployeeByEmail(request.email())){
            throw new EmailAlreadyExists("Email already in use: " + request.email());
        }
        JobPosition position = JobPosition.fromDisplayName(request.jobPosition());
        Employee newEmployee = employeeMapper.toEntity(request);
        newEmployee.setInstitution(institution);
        newEmployee.setJobPosition(position);
        newEmployee.setHiringDate(LocalDate.now());
        newEmployee.setStatus(PersonStatus.ACTIVE);
        newEmployee.setCreatedAt(LocalDateTime.now());

        Employee savedEmployee = employeeRepository.save(newEmployee);
        String defaultPassword = savedEmployee.getDocument().getNumber();

        var employeeRole = roleRepository.findByName(RoleName.ROLE_EMPLOYEE)
                .orElseThrow(() -> new InvalidRoleAssignmentException("Employee Role not found in the system."));
        CreateUserRequestDto userRequest = new CreateUserRequestDto(
                savedEmployee.getEmail(),
                defaultPassword,
                savedEmployee.getId(),
                Set.of(employeeRole.getId())
        );
        userService.create(userRequest);
        return employeeMapper.toDto(savedEmployee);
    }

    @Override
    @Transactional
    @LogActivity("Criou um novo funcionário administrador.")
    public InstitutionAdminResponseDto createAdmin(CreateInstitutionAdminRequestDto request) {
        Institution institution = findInstitutionByIdOrThrow(request.institutionId());
        if (institutionAdminRepository.existsInstitutionAdminByEmail((request.email()))) {
            throw new EmailAlreadyExists("Email already in use: " + request.email());
        }
        InstitutionAdmin newInstitutionAdmin = institutionAdminMapper.toEntity(request);
        newInstitutionAdmin.setInstitution(institution);
        newInstitutionAdmin.setStatus(PersonStatus.ACTIVE);
        newInstitutionAdmin.setCreatedAt(LocalDateTime.now());

        InstitutionAdmin savedInstitutionAdmin = institutionAdminRepository.save(newInstitutionAdmin);
        String defaultPassword = request.document().number();

        List<Role> allRoles = roleRepository.findAll();
        Set<Long> allRoleIds = allRoles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());

        CreateUserRequestDto userRequest = new CreateUserRequestDto(
                savedInstitutionAdmin.getEmail(),
                defaultPassword,
                savedInstitutionAdmin.getId(),
                allRoleIds
        );
        userService.create(userRequest);
        return institutionAdminMapper.toDto(savedInstitutionAdmin);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonResponseDto findById(Long id) {
        Person staffMember = personRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Staff member not found with ID: " + id));

        if (!(staffMember instanceof Employee || staffMember instanceof InstitutionAdmin)) {
            throw new InvalidRoleAssignmentException("The person found does not belong to the staff.");
        }
        return personMapper.toResponseDto(staffMember);
    }

    @Override
    public Page<EmployeeSummaryDto> findPaginated(Long institutionId, Pageable pageable) {
        Page<Employee> employeePage;

        if (institutionId != null) {
            employeePage = employeeRepository.findByInstitutionId(institutionId, pageable);
        } else {
            employeePage = employeeRepository.findAll(pageable);
        }
        return employeePage.map(employeeMapper::toSummaryDto);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou dados de um funcionário.")
    public EmployeeResponseDto update(Long id, UpdateEmployeeRequestDto request) {
        Employee employeeToUpdate = findEmployeeByIdOrThrow(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByLogin(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("Logged in user not found."));

        boolean isSelfUpdate = currentUser.getPerson().getId().equals(id);
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleName.ROLE_ADMIN);

        if (!isAdmin && !isSelfUpdate) {
            throw new AccessDeniedException("You do not have permission to edit another user's profile.");
        }

        if (isAdmin) {
            employeeMapper.updateFromDto(request, employeeToUpdate);

            if (request.departmentId() != null) {
                Department department = departmentRepository.findById(request.departmentId())
                        .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + request.departmentId()));
                employeeToUpdate.setDepartment(department);
            }

        } else {
            if (request.jobPosition() != null || request.status() != null || request.departmentId() != null) {
                throw new AccessDeniedException("You are not allowed to change your job title, status, or department.");
            }
            if (request.email() != null) {
                employeeToUpdate.setEmail(request.email());
            }
            if (request.phone() != null) {
                employeeToUpdate.setPhone(request.phone());
            }
        }

        Employee updatedEmployee = employeeRepository.save(employeeToUpdate);
        return employeeMapper.toDto(updatedEmployee);
    }

    @Override
    @Transactional
    @LogActivity("Deletou um funcionário.")
    public void delete(Long id) {
        if (!personRepository.existsById(id)) {
            throw new UserNotFoundException("Staff member not found with ID: " + id);
        }
        personRepository.deleteById(id);
    }

    public Page<EmployeeSummaryDto> findAllPaginated(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return employeePage.map(employeeMapper::toSummaryDto);
    }

    private Institution findInstitutionByIdOrThrow(Long id) {
        return institutionRepository.findById(id)
                .orElseThrow(() -> new InstitutionNotFoundException("Institution not found with id: " + id));
    }

    private Employee findEmployeeByIdOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }
}

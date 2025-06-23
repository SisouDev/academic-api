package com.institution.management.academic_api.services.employee;

import com.institution.management.academic_api.application.dto.common.DocumentDto;
import com.institution.management.academic_api.application.dto.employee.CreateEmployeeRequestDto;
import com.institution.management.academic_api.application.dto.employee.EmployeeResponseDto;
import com.institution.management.academic_api.application.dto.institution.CreateInstitutionAdminRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionAdminResponseDto;
import com.institution.management.academic_api.application.dto.user.CreateUserRequestDto;
import com.institution.management.academic_api.application.mapper.simple.employee.EmployeeMapper;
import com.institution.management.academic_api.application.mapper.simple.institution.InstitutionAdminMapper;
import com.institution.management.academic_api.application.service.employee.EmployeeServiceImpl;
import com.institution.management.academic_api.domain.model.entities.common.Document;
import com.institution.management.academic_api.domain.model.entities.common.Role;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.entities.institution.InstitutionAdmin;
import com.institution.management.academic_api.domain.model.enums.common.DocumentType;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.common.RoleRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionAdminRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.domain.service.user.UserService;
import com.institution.management.academic_api.exception.type.common.EmailAlreadyExists;
import com.institution.management.academic_api.exception.type.institution.InstitutionNotFoundException;
import com.institution.management.academic_api.exception.type.user.InvalidRoleAssignmentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private InstitutionRepository institutionRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserService userService;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private InstitutionAdminRepository institutionAdminRepository;
    @Mock
    private InstitutionAdminMapper institutionAdminMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private CreateEmployeeRequestDto requestDto;
    private Institution institution;
    private Employee newEmployee;
    private Employee savedEmployee;
    private Role userRole;
    private CreateInstitutionAdminRequestDto adminRequestDto;
    private InstitutionAdmin newAdmin;
    private InstitutionAdmin savedAdmin;

    @BeforeEach
    void setUp() {
        institution = new Institution();
        institution.setId(1L);

        requestDto = new CreateEmployeeRequestDto(
                "Roberto", "Gomes", "roberto.gomes@example.com",
                "Coordinator", LocalDate.now(), 1L,
                new DocumentDto("OTHER", "33344455566")
        );

        newEmployee = new Employee();

        savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setEmail("roberto.gomes@example.com");
        savedEmployee.setDocument(new Document(DocumentType.OTHER, "33344455566"));

        userRole = new Role(RoleName.ROLE_USER);
        userRole.setId(2L);
        adminRequestDto = new CreateInstitutionAdminRequestDto(
                1L, "Admin", "Master", "admin.master@example.com", "11999998888",
                new DocumentDto("OTHER", "000000000")
        );

        newAdmin = new InstitutionAdmin();
        savedAdmin = new InstitutionAdmin();
        savedAdmin.setId(2L);
        savedAdmin.setEmail(adminRequestDto.email());
        savedAdmin.setDocument(new Document(null, "000000000"));
    }

    @Test
    @DisplayName("Deve criar um funcionário e um usuário com sucesso")
    void createEmployee_deveCriarFuncionarioEUsuario_quandoDadosValidos() {
        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institution));
        when(employeeRepository.existsEmployeeByEmail(requestDto.email())).thenReturn(false);
        when(employeeMapper.toEntity(requestDto)).thenReturn(newEmployee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(userRole));
        when(employeeMapper.toDto(savedEmployee)).thenReturn(new EmployeeResponseDto());

        employeeService.createEmployee(requestDto);

        verify(employeeRepository, times(1)).save(newEmployee);
        verify(userService, times(1)).create(any(CreateUserRequestDto.class));
    }

    @Test
    @DisplayName("Deve lançar EmailAlreadyExists ao tentar criar funcionário com email duplicado")
    void createEmployee_deveLancarExcecao_quandoEmailJaExiste() {
        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institution));
        when(employeeRepository.existsEmployeeByEmail(requestDto.email())).thenReturn(true);

        assertThrows(EmailAlreadyExists.class, () -> {
            employeeService.createEmployee(requestDto);
        });

        verify(employeeRepository, never()).save(any());
        verify(userService, never()).create(any());
    }

    @Test
    @DisplayName("Deve lançar InstitutionNotFoundException quando a instituição não existe")
    void createEmployee_deveLancarExcecao_quandoInstituicaoNaoExiste() {
        var requestDtoComIdInexistente = new CreateEmployeeRequestDto(
                "Roberto", "Gomes", "roberto.gomes@example.com",
                "Coordinator", LocalDate.now(), 99L,
                new DocumentDto("OTHER", "33344455566")
        );
        when(institutionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(InstitutionNotFoundException.class, () -> {
            employeeService.createEmployee(requestDtoComIdInexistente);
        });

        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar InvalidRoleAssignmentException quando a role de User não é encontrada")
    void createEmployee_deveLancarExcecao_quandoRoleNaoExiste() {
        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institution));
        when(employeeRepository.existsEmployeeByEmail(requestDto.email())).thenReturn(false);
        when(employeeMapper.toEntity(requestDto)).thenReturn(newEmployee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.empty());

        assertThrows(InvalidRoleAssignmentException.class, () -> {
            employeeService.createEmployee(requestDto);
        });

        verify(userService, never()).create(any());
    }

    @Test
    @DisplayName("createAdmin | Deve criar um admin e um usuário com sucesso")
    void createAdmin_deveCriarAdminEUsuario_quandoDadosValidos() {
        Role adminRole = new Role(RoleName.ROLE_ADMIN);
        adminRole.setId(1L);

        when(institutionRepository.findById(1L)).thenReturn(Optional.of(new Institution()));
        when(institutionAdminRepository.existsInstitutionAdminByEmail(adminRequestDto.email())).thenReturn(false);
        when(institutionAdminMapper.toEntity(adminRequestDto)).thenReturn(newAdmin);
        when(institutionAdminRepository.save(any(InstitutionAdmin.class))).thenReturn(savedAdmin);
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));
        when(institutionAdminMapper.toDto(savedAdmin)).thenReturn(new InstitutionAdminResponseDto());

        employeeService.createAdmin(adminRequestDto);

        verify(institutionAdminRepository, times(1)).save(newAdmin);
        verify(userService, times(1)).create(any(CreateUserRequestDto.class));
    }

    @Test
    @DisplayName("createAdmin | Deve lançar EmailAlreadyExists ao tentar criar admin com email duplicado")
    void createAdmin_deveLancarExcecao_quandoEmailJaExiste() {
        when(institutionRepository.findById(1L)).thenReturn(Optional.of(new Institution()));
        when(institutionAdminRepository.existsInstitutionAdminByEmail(adminRequestDto.email())).thenReturn(true);

        assertThrows(EmailAlreadyExists.class, () -> {
            employeeService.createAdmin(adminRequestDto);
        });

        verify(institutionAdminRepository, never()).save(any());
        verify(userService, never()).create(any());
    }
}
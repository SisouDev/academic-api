package com.institution.management.academic_api.services.teacher;

import com.institution.management.academic_api.application.dto.common.DocumentDto;
import com.institution.management.academic_api.application.dto.teacher.CreateTeacherRequestDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherResponseDto;
import com.institution.management.academic_api.application.dto.user.CreateUserRequestDto;
import com.institution.management.academic_api.application.mapper.simple.teacher.TeacherMapper;
import com.institution.management.academic_api.application.service.teacher.TeacherServiceImpl;
import com.institution.management.academic_api.domain.model.entities.common.Document;
import com.institution.management.academic_api.domain.model.entities.common.Role;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.enums.common.DocumentType;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import com.institution.management.academic_api.domain.model.enums.teacher.AcademicDegree;
import com.institution.management.academic_api.domain.repository.common.RoleRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.domain.repository.teacher.TeacherRepository;
import com.institution.management.academic_api.domain.service.user.UserService;
import com.institution.management.academic_api.exception.type.common.EmailAlreadyExists;
import com.institution.management.academic_api.exception.type.user.InvalidRoleAssignmentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {

    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private InstitutionRepository institutionRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserService userService;
    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    private CreateTeacherRequestDto requestDto;
    private Institution institution;
    private Teacher newTeacher;
    private Teacher savedTeacher;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        institution = new Institution();
        institution.setId(1L);

        requestDto = new CreateTeacherRequestDto(
                "Marco", "Rossi", "marco.rossi@example.com",
                AcademicDegree.PHD.name(), 1L, new DocumentDto("NATIONAL_ID", "12345")
        );

        newTeacher = new Teacher();

        savedTeacher = new Teacher();
        savedTeacher.setId(1L);
        savedTeacher.setEmail("marco.rossi@example.com");
        savedTeacher.setDocument(new Document(DocumentType.NATIONAL_ID, "12345"));

        adminRole = new Role(RoleName.ROLE_ADMIN);
        adminRole.setId(1L);
    }

    @Test
    @DisplayName("Deve criar um professor e um usuário com sucesso")
    void create_deveCriarProfessorEUsuario_quandoDadosValidos() {
        // Arrange
        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institution));
        when(teacherRepository.existsByEmail(requestDto.email())).thenReturn(false);
        when(teacherMapper.toEntity(requestDto)).thenReturn(newTeacher);
        when(teacherRepository.save(any(Teacher.class))).thenReturn(savedTeacher);
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));
        when(teacherMapper.toResponseDto(savedTeacher)).thenReturn(new TeacherResponseDto());

        teacherService.create(requestDto);

        verify(teacherRepository, times(1)).save(newTeacher);
        verify(userService, times(1)).create(any(CreateUserRequestDto.class));
    }

    @Test
    @DisplayName("Deve lançar EmailAlreadyExists ao tentar criar professor com email duplicado")
    void create_deveLancarExcecao_quandoEmailJaExiste() {
        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institution));
        when(teacherRepository.existsByEmail(requestDto.email())).thenReturn(true);

        assertThrows(EmailAlreadyExists.class, () -> {
            teacherService.create(requestDto);
        });

        verify(teacherRepository, never()).save(any());
        verify(userService, never()).create(any());
    }

    @Test
    @DisplayName("Deve lançar InvalidRoleAssignmentException quando a role de Admin não é encontrada")
    void create_deveLancarExcecao_quandoRoleNaoExiste() {
        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institution));
        when(teacherRepository.existsByEmail(requestDto.email())).thenReturn(false);
        when(teacherMapper.toEntity(requestDto)).thenReturn(newTeacher);
        when(teacherRepository.save(any(Teacher.class))).thenReturn(savedTeacher);
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(Optional.empty());

        assertThrows(InvalidRoleAssignmentException.class, () -> {
            teacherService.create(requestDto);
        });

        verify(userService, never()).create(any());
    }
}
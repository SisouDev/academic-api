package com.institution.management.academic_api.services.student;

import com.institution.management.academic_api.application.dto.common.AddressDto;
import com.institution.management.academic_api.application.dto.common.DocumentDto;
import com.institution.management.academic_api.application.dto.student.CreateStudentRequestDto;
import com.institution.management.academic_api.application.dto.student.StudentResponseDto;
import com.institution.management.academic_api.application.dto.user.CreateUserRequestDto;
import com.institution.management.academic_api.application.mapper.simple.student.StudentMapper;
import com.institution.management.academic_api.application.service.student.StudentServiceImpl;
import com.institution.management.academic_api.domain.model.entities.common.Document;
import com.institution.management.academic_api.domain.model.entities.common.Role;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import com.institution.management.academic_api.domain.repository.common.RoleRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.domain.repository.student.StudentRepository;
import com.institution.management.academic_api.domain.service.user.UserService;
import com.institution.management.academic_api.exception.type.common.EmailAlreadyExists;
import com.institution.management.academic_api.exception.type.institution.InstitutionNotFoundException;
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
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private InstitutionRepository institutionRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserService userService;
    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    private CreateStudentRequestDto requestDto;
    private Institution institution;
    private Student newStudent;
    private Student savedStudent;
    private Role userRole;

    @BeforeEach
    void setUp() {
        institution = new Institution();
        institution.setId(1L);

        requestDto = new CreateStudentRequestDto(
                "João", "da Silva", "joao.silva@example.com",
                LocalDate.of(2005, 1, 1), 1L,
                new DocumentDto("NATIONAL_ID", "111222333"),
                new AddressDto("Rua Teste", "123", null, "Bairro", "Cidade", "SP", "12345-678")
        );

        newStudent = new Student();

        savedStudent = new Student();
        savedStudent.setId(1L);
        savedStudent.setEmail("joao.silva@example.com");
        savedStudent.setDocument(new Document(null, "111222333"));

        userRole = new Role(RoleName.ROLE_USER);
        userRole.setId(2L);
    }

    @Test
    @DisplayName("Deve criar um aluno e um usuário com sucesso quando os dados são válidos")
    void create_deveCriarAlunoEUsuario_quandoDadosValidos() {
        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institution));
        when(studentRepository.existsByEmail(requestDto.email())).thenReturn(false);
        when(studentMapper.toEntity(requestDto)).thenReturn(newStudent);
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(userRole));
        when(studentMapper.toResponseDto(savedStudent)).thenReturn(new StudentResponseDto());

        studentService.create(requestDto);

        verify(studentRepository, times(1)).save(newStudent);
        verify(userService, times(1)).create(any(CreateUserRequestDto.class));
    }

    @Test
    @DisplayName("Deve lançar EmailAlreadyExists ao tentar criar aluno com email duplicado")
    void create_deveLancarExcecao_quandoEmailJaExiste() {
        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institution));
        when(studentRepository.existsByEmail(requestDto.email())).thenReturn(true);

        assertThrows(EmailAlreadyExists.class, () -> {
            studentService.create(requestDto);
        });

        verify(studentRepository, never()).save(any());
        verify(userService, never()).create(any());
    }

    @Test
    @DisplayName("Deve lançar InstitutionNotFoundException quando a instituição não existe")
    void create_deveLancarExcecao_quandoInstituicaoNaoExiste() {
        var requestDtoComIdInexistente = new CreateStudentRequestDto(
                "João", "da Silva", "joao.silva@example.com",
                LocalDate.of(2005, 1, 1), 99L,
                new DocumentDto("NATIONAL_ID", "111222333"), null
        );
        when(institutionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(InstitutionNotFoundException.class, () -> {
            studentService.create(requestDtoComIdInexistente);
        });

        verify(studentRepository, never()).save(any());
    }
}
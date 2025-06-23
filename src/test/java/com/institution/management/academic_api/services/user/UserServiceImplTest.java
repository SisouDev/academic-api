package com.institution.management.academic_api.services.user;

import com.institution.management.academic_api.application.dto.user.ChangePasswordRequestDto;
import com.institution.management.academic_api.application.dto.user.CreateUserRequestDto;
import com.institution.management.academic_api.application.mapper.simple.user.UserMapper;
import com.institution.management.academic_api.application.service.user.UserServiceImpl;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.common.Role;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.common.RoleRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.exception.type.user.InvalidPasswordException;
import com.institution.management.academic_api.exception.type.user.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private Person person;
    private Role role;

    @BeforeEach
    void setUp() {
        person = new Person() {
            @Override
            public com.institution.management.academic_api.domain.model.enums.common.PersonType getPersonType() {
                return null;
            }
        };
        person.setId(1L);

        role = new Role(RoleName.ROLE_USER);
        role.setId(2L);
    }

    @Test
    @DisplayName("create | Deve criar um usuário com sucesso")
    void create_deveCriarUsuario_quandoDadosValidos() {
        var request = new CreateUserRequestDto("new.user", "password123", 1L, Set.of(2L));
        when(userRepository.existsByLogin(request.login())).thenReturn(false);
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(roleRepository.findAllById(request.roleIds())).thenReturn(Collections.singletonList(role));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userMapper.toResponseDto(any(User.class))).thenReturn(null);

        userService.create(request);

        verify(userRepository).save(argThat(user ->
                user.getLogin().equals("new.user") &&
                        user.getPasswordHash().equals("hashedPassword") &&
                        user.getPerson().getId().equals(1L)
        ));
    }

    @Test
    @DisplayName("create | Deve lançar UserAlreadyExistsException se o login já existir")
    void create_deveLancarExcecao_quandoLoginJaExiste() {
        var request = new CreateUserRequestDto("existing.user", "password123", 1L, Set.of(2L));
        when(userRepository.existsByLogin("existing.user")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(request));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("changePassword | Deve alterar a senha com sucesso")
    void changePassword_deveAlterarSenha_quandoSenhaAntigaCorreta() {
        var user = new User();
        user.setId(1L);
        user.setPasswordHash("oldHashedPassword");
        var request = new ChangePasswordRequestDto("oldPassword", "newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "oldHashedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newHashedPassword");

        userService.changePassword(1L, request);

        assertEquals("newHashedPassword", user.getPasswordHash());
    }

    @Test
    @DisplayName("changePassword | Deve lançar InvalidPasswordException se a senha antiga estiver incorreta")
    void changePassword_deveLancarExcecao_quandoSenhaAntigaIncorreta() {
        var user = new User();
        user.setId(1L);
        user.setPasswordHash("oldHashedPassword");
        var request = new ChangePasswordRequestDto("wrongOldPassword", "newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongOldPassword", "oldHashedPassword")).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> userService.changePassword(1L, request));
    }
}
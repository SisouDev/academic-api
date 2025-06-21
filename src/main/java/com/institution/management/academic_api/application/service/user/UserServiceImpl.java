package com.institution.management.academic_api.application.service.user;

import com.institution.management.academic_api.application.dto.user.*;
import com.institution.management.academic_api.domain.repository.common.RoleRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto create(CreateUserRequestDto request) {
        System.out.println(">>> Lógica de CRIAR USUÁRIO a ser implementada aqui!");
        return null;
    }

    @Override
    public UserResponseDto findById(Long id) {
        // TODO: Implementar a busca por ID
        return null;
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequestDto request) {
        // TODO: Implementar a lógica de troca de senha
    }

    @Override
    public void updateStatus(Long userId, UpdateUserStatusRequestDto request) {
        // TODO: Implementar a lógica de ativação/desativação
    }

    @Override
    public UserResponseDto assignRoles(Long userId, UpdateUserRolesRequestDto request) {
        // TODO: Implementar a lógica de atribuição de perfis
        return null;
    }
}

package com.institution.management.academic_api.domain.service.user;

import com.institution.management.academic_api.application.dto.user.*;

public interface UserService {
    UserResponseDto create(CreateUserRequestDto request);

    UserResponseDto findById(Long id);

    void changePassword(Long userId, ChangePasswordRequestDto request);

    void updateStatus(Long userId, UpdateUserStatusRequestDto request);

    UserResponseDto assignRoles(Long userId, UpdateUserRolesRequestDto request);
}

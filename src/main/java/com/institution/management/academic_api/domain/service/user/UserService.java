package com.institution.management.academic_api.domain.service.user;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.user.*;
import com.institution.management.academic_api.domain.model.entities.common.ActivityLog;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserResponseDto create(CreateUserRequestDto request);

    UserResponseDto findById(Long id);

    void changePassword(Long userId, ChangePasswordRequestDto request);

    void updateStatus(Long userId, UpdateUserStatusRequestDto request);

    UserResponseDto assignRoles(Long userId, UpdateUserRolesRequestDto request);

    void updateProfilePicture(Long userId, MultipartFile file);

    List<ActivityLog> findUserActivity(Long userId);

    void adminResetPassword(Long userId);

    List<PersonSummaryDto> findSelectableParticipants();

}

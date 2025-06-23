package com.institution.management.academic_api.application.controller.user;

import com.institution.management.academic_api.application.dto.user.ChangePasswordRequestDto;
import com.institution.management.academic_api.application.dto.user.UpdateUserRolesRequestDto;
import com.institution.management.academic_api.application.dto.user.UpdateUserStatusRequestDto;
import com.institution.management.academic_api.application.dto.user.UserResponseDto;
import com.institution.management.academic_api.domain.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody @Valid UpdateUserStatusRequestDto request) {
        userService.updateStatus(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<UserResponseDto> assignRoles(@PathVariable Long id, @RequestBody @Valid UpdateUserRolesRequestDto request) {
        return ResponseEntity.ok(userService.assignRoles(id, request));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changeOwnPassword(
            @AuthenticationPrincipal com.institution.management.academic_api.domain.model.entities.user.User currentUser,
            @RequestBody @Valid ChangePasswordRequestDto request
    ) {
        userService.changePassword(currentUser.getId(), request);
        return ResponseEntity.ok().build();
    }
}
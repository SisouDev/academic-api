package com.institution.management.academic_api.application.controller.user;

import com.institution.management.academic_api.application.dto.user.*;
import com.institution.management.academic_api.domain.model.entities.common.ActivityLog;
import com.institution.management.academic_api.domain.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseDto>> findById(@PathVariable Long id) {
        UserResponseDto userDto = userService.findById(id);

        EntityModel<UserResponseDto> model = EntityModel.of(userDto,
                linkTo(methodOn(UserController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).updateStatus(id, null)).withRel("update-status"),
                linkTo(methodOn(UserController.class).assignRoles(id, null)).withRel("assign-roles")
        );

        return ResponseEntity.ok(model);
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

    @PostMapping("/me/avatar")
    public ResponseEntity<Void> uploadProfilePicture(
            @AuthenticationPrincipal com.institution.management.academic_api.domain.model.entities.user.User currentUser,
            @RequestParam("file") MultipartFile file) {

        userService.updateProfilePicture(currentUser.getId(), file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityLog>> getUserActivities(@PathVariable Long id) {
        List<ActivityLog> activities = userService.findUserActivity(id);
        return ResponseEntity.ok(activities);
    }

    @PatchMapping("/me")
    @Operation(summary = "Atualiza informações do perfil do usuário autenticado")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updateMyProfile(@RequestBody @Valid UpdateProfileRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        userService.updateMyProfile(userEmail, request);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/me")
    @Operation(summary = "Busca os detalhes do perfil do usuário atualmente autenticado")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EntityModel<UserResponseDto>> findMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        UserResponseDto userDto = userService.findMyProfile(userEmail);

        EntityModel<UserResponseDto> model = EntityModel.of(userDto,
                linkTo(methodOn(UserController.class).findMyProfile()).withSelfRel(),
                linkTo(methodOn(UserController.class).updateMyProfile(null)).withRel("update-profile"),
                linkTo(methodOn(UserController.class).changeOwnPassword(null, null)).withRel("change-password"),
                linkTo(methodOn(UserController.class).uploadProfilePicture(null, null)).withRel("upload-avatar")
        );

        return ResponseEntity.ok(model);
    }

}
package com.institution.management.academic_api.application.service.user;

import com.institution.management.academic_api.application.dto.user.*;
import com.institution.management.academic_api.application.mapper.simple.user.UserMapper;
import com.institution.management.academic_api.application.service.FileStorageService;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.common.Role;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.common.RoleRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.user.UserService;
import com.institution.management.academic_api.exception.type.user.InvalidPasswordException;
import com.institution.management.academic_api.exception.type.user.InvalidRoleAssignmentException;
import com.institution.management.academic_api.exception.type.user.UserAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public UserResponseDto create(CreateUserRequestDto request) {
        if (userRepository.existsByLogin(request.login())) {
            throw new UserAlreadyExistsException("Login already in use: " + request.login());
        }
        Person person = personRepository.findById(request.personId())
                .orElseThrow(() -> new EntityNotFoundException("Person not found to associate with user. ID: " + request.personId()));

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.roleIds()));
        if (roles.isEmpty()) {
            throw new InvalidRoleAssignmentException("No valid Role was provided for the user.");
        }

        User newUser = new User();
        newUser.setLogin(request.login());
        newUser.setPerson(person);
        newUser.setRoles(roles);
        newUser.setActive(true);

        String hashedPassword = passwordEncoder.encode(request.password());
        newUser.setPasswordHash(hashedPassword);

        User savedUser = userRepository.save(newUser);

        return userMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("The old password is incorrect.");
        }

        String newHashedPassword = passwordEncoder.encode(request.newPassword());
        user.setPasswordHash(newHashedPassword);

    }

    @Override
    @Transactional
    public void updateStatus(Long userId, UpdateUserStatusRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        user.setActive(request.active());
    }

    @Override
    @Transactional
    public UserResponseDto assignRoles(Long userId, UpdateUserRolesRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Set<Role> newRoles = new HashSet<>(roleRepository.findAllById(request.roleIds()));
        if (newRoles.isEmpty()) {
            throw new InvalidRoleAssignmentException("No valid Role was provided.");
        }

        user.setRoles(newRoles);
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public void updateProfilePicture(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        String fileUrl = fileStorageService.store(file);

        personRepository.updateProfilePictureUrl(user.getPerson().getId(), fileUrl);
    }
}

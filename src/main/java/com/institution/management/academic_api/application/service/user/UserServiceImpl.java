package com.institution.management.academic_api.application.service.user;

import com.institution.management.academic_api.application.dto.common.PersonResponseDto;
import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.file.FileUploadResponseDto;
import com.institution.management.academic_api.application.dto.user.*;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.application.mapper.simple.employee.EmployeeMapper;
import com.institution.management.academic_api.application.mapper.simple.student.StudentMapper;
import com.institution.management.academic_api.application.mapper.simple.teacher.TeacherMapper;
import com.institution.management.academic_api.application.mapper.simple.user.UserMapper;
import com.institution.management.academic_api.application.notifiers.user.UserNotifier;
import com.institution.management.academic_api.domain.model.entities.common.ActivityLog;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.common.Role;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.file.ReferenceType;
import com.institution.management.academic_api.domain.repository.common.ActivityLogRepository;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.common.RoleRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.file.FileUploadService;
import com.institution.management.academic_api.domain.service.user.UserService;
import com.institution.management.academic_api.exception.type.user.InvalidPasswordException;
import com.institution.management.academic_api.exception.type.user.InvalidRoleAssignmentException;
import com.institution.management.academic_api.exception.type.user.UserAlreadyExistsException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final FileUploadService fileUploadService;
    private final ActivityLogRepository activityLogRepository;
    private final UserNotifier userNotifier;
    private final PersonMapper personMapper;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;
    private final EmployeeMapper employeeMapper;



    @Override
    @Transactional
    @LogActivity("Cadastrou um novo usuário.")
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
        userNotifier.notifyUserWelcome(savedUser);

        return userMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        User user = userRepository.findByIdWithPerson(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        return getFullUserProfile(user);
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findMyProfile(String userEmail) {
        User user = userRepository.findByLoginWithFullPerson(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Logged user not found."));

        return getFullUserProfile(user);
    }



    @Override
    @Transactional
    @LogActivity("Alterou a própria senha")
    public void changePassword(Long userId, ChangePasswordRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("The old password is incorrect.");
        }

        String newHashedPassword = passwordEncoder.encode(request.newPassword());
        user.setPasswordHash(newHashedPassword);
        userNotifier.notifyPasswordChange(user);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou o status")
    public void updateStatus(Long userId, UpdateUserStatusRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        user.setActive(request.active());
    }

    @Override
    @Transactional
    @LogActivity("Atribiu funções")
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
    @Transactional(readOnly = true)
    public List<PersonSummaryDto> findSelectableParticipants() {
        return personRepository.findAllNonStudents().stream()
                .map(personMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @LogActivity("Atualizou a foto de perfil")
    public void updateProfilePicture(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + userId));

        FileUploadResponseDto fileResponse = fileUploadService.uploadFile(
                file,
                user.getPerson().getId(),
                ReferenceType.PROFILE_PICTURE
        );

        String fileUrl = fileResponse.fileDownloadUri();

        Person person = user.getPerson();
        person.setProfilePictureUrl(fileUrl);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> findUserActivity(Long userId) {
        return activityLogRepository.findTop5ByUserIdOrderByTimestampDesc(userId);
    }

    @Override
    @Transactional
    @LogActivity("Resetou a senha de um usuário (ação administrativa).")
    public void adminResetPassword(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + userId + " não encontrado."));

        String newPassword = generateRandomPassword(8);
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userNotifier.notifyPasswordReset(user);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou o próprio perfil.")
    public void updateMyProfile(String userEmail, UpdateProfileRequestDto request) {
        User user = userRepository.findByLogin(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuário logado não encontrado."));

        Person person = user.getPerson();

        if (StringUtils.hasText(request.email())) {
            person.setEmail(request.email());
        }

        if (StringUtils.hasText(request.phone())) {
            person.setPhone(request.phone());
        }
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private UserResponseDto getFullUserProfile(User user) {
        UserResponseDto baseResponseDto = userMapper.toResponseDto(user);

        Person personEntity = user.getPerson();

        PersonResponseDto detailedPersonDto = switch (personEntity) {
            case Student student -> {
                Hibernate.initialize(student.getEnrollments());
                Hibernate.initialize(student.getInstitution());
                yield studentMapper.toResponseDto(student);
            }

            case Teacher teacher -> {
                Hibernate.initialize(teacher.getCourseSections());
                Hibernate.initialize(teacher.getSubjects());
                yield teacherMapper.toResponseDto(teacher);
            }

            case Employee employee -> {
                Hibernate.initialize(employee.getJobPosition());
                Hibernate.initialize(employee.getDepartment());
                yield employeeMapper.toDto(employee);
            }

            case null, default -> baseResponseDto.person();
        };

        return new UserResponseDto(
                baseResponseDto.id(),
                baseResponseDto.login(),
                baseResponseDto.active(),
                detailedPersonDto,
                baseResponseDto.roles()
        );
    }
}

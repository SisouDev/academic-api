package com.institution.management.academic_api.application.service.teacher;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import com.institution.management.academic_api.application.dto.teacher.CreateTeacherRequestDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherResponseDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherSummaryDto;
import com.institution.management.academic_api.application.dto.teacher.UpdateTeacherRequestDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.application.mapper.simple.course.CourseSectionMapper;
import com.institution.management.academic_api.application.mapper.simple.teacher.TeacherMapper;
import com.institution.management.academic_api.domain.model.entities.common.Role;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.entities.specification.TeacherSpecification;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import com.institution.management.academic_api.domain.model.enums.teacher.AcademicDegree;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.common.RoleRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.domain.repository.teacher.TeacherRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.teacher.TeacherService;
import com.institution.management.academic_api.domain.service.user.UserService;
import com.institution.management.academic_api.exception.type.common.EmailAlreadyExists;
import com.institution.management.academic_api.exception.type.institution.InstitutionNotFoundException;
import com.institution.management.academic_api.exception.type.teacher.TeacherNotFoundException;
import com.institution.management.academic_api.exception.type.user.InvalidRoleAssignmentException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final InstitutionRepository institutionRepository;
    private final UserService userService;
    private final PersonMapper personMapper;
    private final RoleRepository roleRepository;
    private final CourseSectionMapper courseSectionMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    @Override
    @Transactional
    @LogActivity("Cadastrou um novo professor")
    public TeacherResponseDto create(CreateTeacherRequestDto request) {
        Institution institution = findInstitutionByIdOrThrow(request.institutionId());

        if (personRepository.existsByEmail(request.email())){
            throw new EmailAlreadyExists("Email already in use: " + request.email());
        }

        Teacher newTeacher = teacherMapper.toEntity(request);
        if (request.academicBackground() == null || request.academicBackground().isEmpty()){
            newTeacher.setAcademicBackground(AcademicDegree.LICENTIATE);
        }
        newTeacher.setInstitution(institution);
        newTeacher.setStatus(PersonStatus.ACTIVE);
        newTeacher.setCreatedAt(LocalDateTime.now());
        Teacher savedTeacher = teacherRepository.save(newTeacher);

        User newUser = new User();
        String defaultPassword = savedTeacher.getDocument().getNumber();

        newUser.setLogin(savedTeacher.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(defaultPassword));
        newUser.setActive(true);
        newUser.setPerson(savedTeacher);

        Role teacherRole = roleRepository.findByName(RoleName.ROLE_TEACHER)
                .orElseThrow(() -> new InvalidRoleAssignmentException("Teacher Role not found in the system."));
        newUser.setRoles(Collections.singleton(teacherRole));

        userRepository.save(newUser);

        return teacherMapper.toResponseDto(savedTeacher);
    }


    @Override
    @Transactional(readOnly = true)
    public TeacherResponseDto findById(Long id) {
        Teacher teacher = findTeacherByIdOrThrow(id);
        return teacherMapper.toResponseDto(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonSummaryDto> findAllByInstitution(Long institutionId) {
        Institution institution = findInstitutionByIdOrThrow(institutionId);
        List<Teacher> teachers = teacherRepository.findAllByInstitution(institution);
        return teachers.stream()
                .map(personMapper::toSummaryDto)
                .toList();
    }

    @Override
    @Transactional
    @LogActivity("Atualizou um professor")
    public TeacherResponseDto update(Long id, UpdateTeacherRequestDto request) {
        Teacher teacherToUpdate = findTeacherByIdOrThrow(id);
        teacherMapper.updateFromDto(request, teacherToUpdate);
        Teacher updatedTeacher = teacherRepository.save(teacherToUpdate);
        return teacherMapper.toResponseDto(updatedTeacher);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeacherSummaryDto> findPaginated(String searchTerm, Pageable pageable) {
        Specification<Teacher> spec = TeacherSpecification.searchByTerm(searchTerm);
        return teacherRepository.findAll(spec, pageable).map(teacherMapper::toSummaryDto);
    }


    private Institution findInstitutionByIdOrThrow(Long id) {
        return institutionRepository.findById(id)
                .orElseThrow(() -> new InstitutionNotFoundException("Institution not found with id: " + id));
    }

    private Teacher findTeacherByIdOrThrow(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseSectionSummaryDto> findCourseSectionsByTeacherId(Long teacherId) {
        Teacher teacher = findTeacherByIdOrThrow(teacherId);
        return teacher.getCourseSections().stream()
                .map(courseSectionMapper::toSummaryDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseSectionSummaryDto> findSectionsForCurrentTeacher() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Teacher teacher = teacherRepository.findByEmail(username)
                .orElseThrow(() -> new AccessDeniedException("The logged in user is not a teacher or was not found."));

        return teacher.getCourseSections().stream()
                .filter(section -> {
                    if (section.getAcademicTerm() == null) return false;
                    AcademicTermStatus status = section.getAcademicTerm().getStatus();
                    return status == AcademicTermStatus.ENROLLMENT_OPEN || status == AcademicTermStatus.IN_PROGRESS;
                })
                .map(courseSectionMapper::toSummaryDto)
                .collect(Collectors.toList());
    }
}

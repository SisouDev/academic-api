package com.institution.management.academic_api.application.service.student;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.student.CreateStudentRequestDto;
import com.institution.management.academic_api.application.dto.student.StudentResponseDto;
import com.institution.management.academic_api.application.dto.student.UpdateStudentRequestDto;
import com.institution.management.academic_api.application.dto.user.CreateUserRequestDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.application.mapper.simple.student.StudentMapper;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import com.institution.management.academic_api.domain.repository.common.RoleRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.domain.repository.student.StudentRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.student.StudentService;
import com.institution.management.academic_api.domain.service.user.UserService;
import com.institution.management.academic_api.exception.type.common.EmailAlreadyExists;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.exception.type.institution.InstitutionNotFoundException;
import com.institution.management.academic_api.exception.type.student.StudentNotFoundException;
import com.institution.management.academic_api.exception.type.user.InvalidRoleAssignmentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final InstitutionRepository institutionRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final StudentMapper studentMapper;
    private final PersonMapper personMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public StudentResponseDto create(CreateStudentRequestDto request) {
        Institution institution = findInstitutionByIdOrThrow(request.institutionId());
        if (studentRepository.existsByEmail(request.email())){
            throw new EmailAlreadyExists("Email already in use: " + request.email());
        }
        Student newStudent = studentMapper.toEntity(request);
        newStudent.setInstitution(institution);
        newStudent.setStatus(PersonStatus.ACTIVE);
        newStudent.setCreatedAt(LocalDateTime.now());

        Student savedStudent = studentRepository.save(newStudent);
        String defaultPassword = request.document().number();

        var studentRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new InvalidRoleAssignmentException("User Role not found in the system."));
        CreateUserRequestDto userRequest = new CreateUserRequestDto(
                savedStudent.getEmail(),
                defaultPassword,
                savedStudent.getId(),
                Set.of(studentRole.getId())
        );
        userService.create(userRequest);
        return studentMapper.toResponseDto(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDto findById(Long id) {
        Student student = findStudentByIdOrThrow(id);
        return studentMapper.toResponseDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonSummaryDto> findAllByInstitution(Long institutionId) {
        Institution institution = findInstitutionByIdOrThrow(institutionId);
        List<Student> students = studentRepository.findAllByInstitution(institution);
        return students.stream()
                .map(personMapper::toSummaryDto)
                .toList();
    }

    @Override
    @Transactional
    public StudentResponseDto update(Long id, UpdateStudentRequestDto request) {
        Student studentToUpdate = findStudentByIdOrThrow(id);
        studentMapper.updateFromDto(request, studentToUpdate);
        Student updatedStudent = studentRepository.save(studentToUpdate);
        return studentMapper.toResponseDto(updatedStudent);
    }

    @Override
    @Transactional
    public StudentResponseDto updateStatus(Long id, String status) {
        Student studentToUpdate = findStudentByIdOrThrow(id);

        PersonStatus newStatus = PersonStatus.valueOf(status.toUpperCase());

        User userToUpdate = userRepository.findByPerson(studentToUpdate)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado para o aluno com ID: " + id));

        studentToUpdate.setStatus(newStatus);
        userToUpdate.setActive(newStatus == PersonStatus.ACTIVE);

        studentRepository.save(studentToUpdate);
        userRepository.save(userToUpdate);
        return studentMapper.toResponseDto(studentToUpdate);
    }

    private Institution findInstitutionByIdOrThrow(Long id) {
        return institutionRepository.findById(id)
                .orElseThrow(() -> new InstitutionNotFoundException("Institution not found with id: " + id));
    }

    private Student findStudentByIdOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
    }
}

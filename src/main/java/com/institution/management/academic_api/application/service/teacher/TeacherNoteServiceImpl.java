package com.institution.management.academic_api.application.service.teacher;

import com.institution.management.academic_api.application.dto.teacher.CreateTeacherNoteRequestDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherNoteDto;
import com.institution.management.academic_api.application.mapper.simple.teacher.TeacherNoteMapper;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.entities.teacher.TeacherNote;
import com.institution.management.academic_api.domain.repository.student.EnrollmentRepository;
import com.institution.management.academic_api.domain.repository.teacher.TeacherNoteRepository;
import com.institution.management.academic_api.domain.repository.teacher.TeacherRepository;
import com.institution.management.academic_api.domain.service.teacher.TeacherNoteService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherNoteServiceImpl implements TeacherNoteService {

    private final TeacherNoteRepository noteRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherNoteMapper noteMapper;

    @Override
    @Transactional
    public TeacherNoteDto create(CreateTeacherNoteRequestDto request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Teacher author = teacherRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Logged in teacher not found."));

        Enrollment enrollment = enrollmentRepository.findById(request.enrollmentId())
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found."));
        System.out.println(enrollment);

        TeacherNote newNote = noteMapper.toEntity(request);
        System.out.println(newNote);
        newNote.setAuthor(author);
        newNote.setEnrollment(enrollment);
        newNote.setCreatedAt(LocalDateTime.now());

        TeacherNote savedNote = noteRepository.save(newNote);
        System.out.println(savedNote);

        return noteMapper.toDto(savedNote);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherNoteDto> findByEnrollment(Long enrollmentId) {
        return noteRepository.findAllByEnrollmentIdOrderByCreatedAtDesc(enrollmentId)
                .stream()
                .map(noteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherNoteDto findById(Long noteId) {
        return noteRepository.findById(noteId)
                .map(noteMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Note not found: " + noteId));
    }

    @Override
    @Transactional
    public void delete(Long noteId) {
        noteRepository.deleteById(noteId);
    }
}
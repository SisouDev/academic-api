package com.institution.management.academic_api.domain.service.teacher;

import com.institution.management.academic_api.application.dto.teacher.CreateTeacherNoteRequestDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherNoteDto;

import java.util.List;

public interface TeacherNoteService {
    TeacherNoteDto create(CreateTeacherNoteRequestDto request);
    List<TeacherNoteDto> findByEnrollment(Long enrollmentId);
    void delete(Long noteId);
    TeacherNoteDto findById(Long noteId);
}
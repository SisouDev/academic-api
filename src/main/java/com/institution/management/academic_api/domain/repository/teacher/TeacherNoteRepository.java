package com.institution.management.academic_api.domain.repository.teacher;

import com.institution.management.academic_api.domain.model.entities.teacher.TeacherNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherNoteRepository extends JpaRepository<TeacherNote, Long> {
    List<TeacherNote> findAllByEnrollmentIdOrderByCreatedAtDesc(Long enrollmentId);
    List<TeacherNote> findAllByEnrollmentIdInOrderByCreatedAtDesc(List<Long> enrollmentIds);

}
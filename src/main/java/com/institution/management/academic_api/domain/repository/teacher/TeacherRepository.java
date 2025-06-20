package com.institution.management.academic_api.domain.repository.teacher;

import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
